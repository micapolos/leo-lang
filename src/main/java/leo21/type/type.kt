package leo21.type

import leo.base.notNullOrError
import leo.base.orNullIf
import leo13.onlyOrNull
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo

sealed class Type : Scriptable() {
	override val reflectScriptLine: ScriptLine get() = "type" lineTo script
}

data class StructType(val struct: Struct) : Type() {
	override fun toString() = super.toString()
}

data class ChoiceType(val choice: Choice) : Type() {
	override fun toString() = super.toString()
}

data class RecursiveType(val recursive: Recursive) : Type() {
	override fun toString() = super.toString()
}

fun type(struct: Struct): Type = StructType(struct)
fun type(choice: Choice): Type = ChoiceType(choice)

val Type.structOrNull get() = (this as? StructType)?.struct
val Type.choiceOrNull get() = (this as? ChoiceType)?.choice

val Type.struct get() = structOrNull.notNullOrError("not struct")
val Type.choice get() = choiceOrNull.notNullOrError("not choice")

fun Type.plus(line: Line): Type = type(struct.plus(line))
fun type(vararg lines: Line) = type(struct(*lines))

val stringType = type(stringLine)
val doubleType = type(doubleLine)

fun Type.make(name: String): Type =
	type(name lineTo this)

val Type.onlyNameOrNull: String?
	get() =
		structOrNull?.lineStack?.onlyOrNull?.fieldOrNull?.orNullIf { rhs != type() }?.name
