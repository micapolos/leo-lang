package leo13.type

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo13.Script
import leo13.ScriptLine
import leo9.*

data class Type(val choiceOrNull: Choice?, val lineStack: Stack<TypeLine>) : Scriptable() {
	override fun toString() = scriptableBody.toString()
	override val scriptableName get() = "type"
	override val scriptableBody get() = asCustomScript
}

data class TypeLine(val name: String, val rhs: Type) {
	override fun toString() = asRawScriptLine.toString()
}

data class TypeLink(val lhs: Type, val line: TypeLine)
data class TypeArrow(val lhs: Type, val rhs: Type)
data class TypeAccess(val int: Int, val type: Type)

// --- constructors

fun type(vararg lines: TypeLine) = Type(null, stack(*lines))
fun type(name: String) = type(name lineTo type())
fun type(choice: Choice, vararg lines: TypeLine) = Type(choice, stack(*lines))
fun Type.plus(line: TypeLine) = Type(choiceOrNull, lineStack.push(line))
infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)
fun access(int: Int, type: Type) = TypeAccess(int, type)
infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)

val Type.asCustomScript: Script
	get() =
		(choiceOrNull?.scriptableLine?.script ?: leo13.script())
			.fold(lineStack.reverse) { plus(it.asScriptLine) }

val TypeLine.asRawScriptLine
	get() =
		name lineTo rhs.scriptableBody

val TypeLine.asScriptLine
	get() =
		if (name == "or" && rhs.onlyLineOrNull != null) "meta" lineTo script(asRawScriptLine)
		else asRawScriptLine

val Type.isEmpty get() = choiceOrNull == null && lineStack.isEmpty

val Type.previousOrNull
	get() =
		lineStack
			.linkOrNull
			?.let { lineStackLink -> Type(choiceOrNull, lineStackLink.stack) }
			?: notNullIf(choiceOrNull != null) { type() }

val Type.lineOrNull
	get() =
		lineStack
			.linkOrNull
			?.let { lineStackLink -> Type(null, stack(lineStackLink.value)) }
			?: choiceOrNull?.let { choice -> Type(choice, stack()) }

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

// --- typeOrNull

val ScriptLine.typeOrNull: Type?
	get() =
		ifOrNull(name == "type") {
			rhs.typeOrNull
		}

val Script.typeOrNull: Type?
	get() =
		lineStack.reverse.let { reverseLineStack ->
			when (reverseLineStack) {
				is EmptyStack -> type()
				is LinkStack ->
					reverseLineStack.link.value.choiceOrNull.let { choiceOrNull ->
						if (choiceOrNull == null) plainTypeOrNull
						else reverseLineStack.link.stack.reverse.script.plainTypeOrNull?.let { plainTypeOrNull ->
							Type(choiceOrNull, plainTypeOrNull.lineStack)
						}
					}
			}
		}

val Script.plainTypeOrNull: Type?
	get() =
		if (lineStack.isEmpty) type()
		else asStackOrNull { typeLineOrNull }?.let { lineStack ->
			Type(null, lineStack)
		}

val ScriptLine.typeLineOrNull: TypeLine?
	get() =
		rhs.typeOrNull?.let { type -> name lineTo type }

val String.unsafeType get() = unsafeScript.typeOrNull!!

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
