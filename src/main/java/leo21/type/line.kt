package leo21.type

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script

interface AsLine : AsType {
	val asLine: Line
	override val asType get() = type(asLine)
}

sealed class Line : Scriptable(), AsLine {
	override val reflectScriptLine: ScriptLine
		get() = "line" lineTo script(scriptLine)
	override val asLine get() = this
}

object StringLine : Line()
object NumberLine : Line()
data class FieldLine(val field: Field) : Line() {
	override fun toString() = super.toString()
}

data class ArrowLine(val arrow: Arrow) : Line() {
	override fun toString() = super.toString()
}

data class ChoiceLine(val choice: Choice) : Line() {
	override fun toString() = super.toString()
}

data class RecursiveLine(val recursive: Recursive) : Line() {
	override fun toString() = super.toString()
}

data class RecurseLine(val recurse: Recurse) : Line() {
	override fun toString() = super.toString()
}

val Line.fieldOrNull: Field? get() = (this as? FieldLine)?.field
val Line.arrowOrNull: Arrow? get() = (this as? ArrowLine)?.arrow
val Line.choiceOrNull: Choice? get() = (this as? ChoiceLine)?.choice
val Line.recursiveOrNull: Recursive? get() = (this as? RecursiveLine)?.recursive
val Line.recurseOrNull: Recurse? get() = (this as? RecurseLine)?.recurse

fun line(arrow: Arrow): Line = ArrowLine(arrow)
fun line(field: Field): Line = FieldLine(field)
fun line(choice: Choice): Line = ChoiceLine(choice)
fun line(recursive: Recursive): Line = RecursiveLine(recursive)
fun line(recurse: Recurse): Line = RecurseLine(recurse)

infix fun String.lineTo(rhs: AsType) = line(this fieldTo rhs.asType)
infix fun Type.lineTo(rhs: AsType) = line(this arrowTo rhs.asType)

val Line.nameOrNull: String?
	get() =
		when (this) {
			StringLine -> "text"
			NumberLine -> "number"
			is FieldLine -> field.name
			is ChoiceLine -> null
			is ArrowLine -> "function"
			is RecursiveLine -> recursive.line.nameOrNull
			is RecurseLine -> null
		}

fun Line.matches(name: String): Boolean =
	nameOrNull?.equals(name) ?: false

val stringLine: Line = StringLine
val numberLine: Line = NumberLine
val equalsLine: Line = "equals" lineTo choice("yes" lineTo type(), "no" lineTo type())