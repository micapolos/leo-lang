package leo13

import leo.base.ifOrNull
import leo9.*

data class Type(val choiceOrNull: Choice?, val lineStack: Stack<TypeLine>) : Scriptable() {
	override fun toString() = asScript.toString()
	override val asScriptLine = "type" lineTo asScript
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
				?.let { case -> type(choice(line.case, case)) }
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

// --- exact type

val Script.exactType: Type
	get() = type().fold(lineStack.reverse) { plus(it.exactTypeLine) }

val ScriptLine.exactTypeLine
	get() = name lineTo rhs.exactType

// --- type matches script

fun Type.matches(script: Script): Boolean =
	contains(script.exactType)

// === type to script

val Type.staticScriptOrNull: Script?
	get() =
		ifOrNull(choiceOrNull == null) {
			lineStack.mapOrNull { staticScriptLineOrNull }?.script
		}

val TypeLine.staticScriptLineOrNull: ScriptLine?
	get() =
		rhs.staticScriptOrNull?.let { name lineTo it }

val TypeLink.staticScriptLinkOrNull: ScriptLink?
	get() =
		lhs.staticScriptOrNull?.let { lhsScript ->
			line.staticScriptLineOrNull?.let { scriptLine ->
				link(lhsScript, scriptLine)
			}
		}

val Type.scriptOrError: Script
	get() =
		staticScriptOrNull ?: error("type is not compile-time constant")

val TypeLink.type
	get() =
		lhs.plus(line)

// --- contains

fun Type.contains(type: Type): Boolean =
	when (lineStack) {
		is EmptyStack ->
			if (choiceOrNull == null) type.isEmpty
			else choiceOrNull.contains(type)
		is LinkStack ->
			when (type.lineStack) {
				is EmptyStack -> false
				is LinkStack -> lineStack.link.value.contains(type.lineStack.link.value)
					&& Type(choiceOrNull, lineStack.link.stack).contains(Type(choiceOrNull, type.lineStack.link.stack))
			}
	}

fun TypeLine.contains(line: TypeLine): Boolean =
	name == line.name && rhs.contains(line.rhs)
