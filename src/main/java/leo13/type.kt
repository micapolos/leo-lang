package leo13

import leo.base.ifNotNull
import leo.base.ifOrNull
import leo.base.orNull
import leo9.*

data class Type(val choiceOrNull: Choice?, val lineStack: Stack<TypeLine>) {
	override fun toString() = asScript.toString()
}

data class TypeLine(val name: String, val rhs: Type) {
	override fun toString() = asRawScriptLine.toString()
}

data class TypeLink(val lhs: Type, val line: TypeLine)
data class TypeArrow(val lhs: Type, val rhs: Type)
data class TypeAccess(val int: Int, val type: Type)

// --- constructors

fun type(vararg lines: TypeLine) = Type(null, stack(*lines))
fun type(choice: Choice, vararg lines: TypeLine) = Type(choice, stack(*lines))
fun Type.plus(line: TypeLine) = Type(choiceOrNull, lineStack.push(line))
infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)
fun access(int: Int, type: Type) = TypeAccess(int, type)
infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)

val Type.asScript: Script
	get() =
		(choiceOrNull?.asScript ?: script()).fold(lineStack.reverse) { plus(it.asScriptLine) }

val TypeLine.asRawScriptLine
	get() =
		name lineTo rhs.asScript

val TypeLine.asScriptLine
	get() =
		if (name == "or" && rhs.onlyLineOrNull != null) "meta" lineTo script(asRawScriptLine)
		else asRawScriptLine

val Type.isEmpty get() = choiceOrNull == null && lineStack.isEmpty

val Type.onlyLineOrNull
	get() =
		ifOrNull(choiceOrNull == null) {
			lineStack.onlyOrNull
		}

val Type.onlyChoiceOrNull
	get() =
		ifOrNull(lineStack.isEmpty) {
			choiceOrNull
		}

// --- script -> type

fun Type.plus(scriptLine: ScriptLine): Type =
	casePlusOrNull(scriptLine) ?: linePlus(scriptLine)

fun Type.casePlusOrNull(scriptLine: ScriptLine): Type? =
	if (choiceOrNull == null)
		lineStack.onlyOrNull?.let { line ->
			scriptLine
				.nextCaseOrNull
				?.let { case -> type(choice(line.firstCase, case)) }
		}
	else ifOrNull(lineStack.isEmpty) {
		choiceOrNull.plusOrNull(scriptLine)?.let { choice ->
			type(choice)
		}
	}

fun Type.linePlus(scriptLine: ScriptLine): Type =
	plus(scriptLine.typeLine)

val Script.type get() = type().fold(lineStack.reverse) { plus(it) }

val ScriptLine.typeLine: TypeLine
	get() =
		name lineTo rhs.type

val Script.exactType
	get() = type().fold(lineStack.reverse) { linePlus(it) }

// --- type matches script

fun Type.matches(script: Script): Boolean =
	script()
		.orNull
		.zipFold(lineStack, script.lineStack) { lineOrNull, scriptLineOrNull ->
			this?.run {
				if (lineOrNull != null)
					scriptLineOrNull.ifNotNull { ifOrNull(lineOrNull.matches(it)) { this } }
				else
					scriptLineOrNull.ifNotNull { plus(it) }
			}
		}
		?.run {
			choiceOrNull
				?.let { choice -> onlyLineOrNull?.let { choice.matches(it) } ?: false }
				?: true
		} ?: false

fun TypeLine.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && rhs.matches(scriptLine.rhs)

fun TypeLink.matches(scriptLink: ScriptLink) =
	lhs.matches(scriptLink.lhs) && line.matches(scriptLink.line)

// type to script

val Type.scriptOrNull: Script?
	get() =
		ifOrNull(choiceOrNull == null) {
			lineStack.mapOrNull { scriptLineOrNull }?.script
		}

val TypeLine.scriptLineOrNull: ScriptLine?
	get() =
		rhs.scriptOrNull?.let { name lineTo it }

val TypeLink.scriptLinkOrNull: ScriptLink?
	get() =
		lhs.scriptOrNull?.let { lhsScript ->
			line.scriptLineOrNull?.let { scriptLine ->
				link(lhsScript, scriptLine)
			}
		}

val Type.scriptOrError: Script
	get() =
		scriptOrNull ?: error("type is not compile-time constant")

val TypeLink.type
	get() =
		lhs.plus(line)

// --- contains

fun Type.contains(type: Type): Boolean =
	lineContains(type) && choiceContains(type)

fun Type.lineContains(type: Type): Boolean =
	true.zipFoldOrNull(lineStack, type.lineStack) { line, typeLine ->
		line.contains(typeLine)
	} ?: false

fun Type.choiceContains(type: Type) =
	if (choiceOrNull != null) type.choiceOrNull != null && choiceOrNull.contains(type.choiceOrNull)
	else type.choiceOrNull == null

fun TypeLine.contains(line: TypeLine): Boolean =
	name == line.name && rhs.contains(line.rhs)
