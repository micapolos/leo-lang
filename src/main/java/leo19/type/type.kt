package leo19.type

import leo.base.foldMapFirstOrNull
import leo.base.indexed
import leo.base.notNullIf
import leo.base.runIf
import leo13.Stack
import leo13.first
import leo13.onlyOrNull
import leo13.push
import leo13.seq
import leo13.stack
import leo14.indentString
import leo14.untyped.pretty.indentString

sealed class Type {
	override fun toString() = reflectScript.indentString
}

data class StructType(val struct: Struct) : Type() {
	override fun toString() = super.toString()
}

data class ChoiceType(val choice: Choice) : Type() {
	override fun toString() = super.toString()
}

data class ArrowType(val arrow: Arrow) : Type() {
	override fun toString() = super.toString()
}

data class Struct(val fieldStack: Stack<Field>) {
	override fun toString() = reflectScript.indentString
}

data class Choice(val caseStack: Stack<Case>) {
	override fun toString() = reflectScript.indentString
}

data class Field(val name: String, val type: Type) {
	override fun toString() = reflectScriptLine.indentString(0)
}

data class Case(val name: String, val type: Type) {
	override fun toString() = reflectScriptLine.indentString(0)
}

data class Arrow(val lhs: Type, val rhs: Type) {
	override fun toString() = reflectScript.indentString
}

fun struct(vararg fields: Field): Type = StructType(Struct(stack(*fields)))
fun struct(name: String) = struct(name fieldTo struct())
fun choice(vararg cases: Case): Type = ChoiceType(Choice(stack(*cases)))
fun choice(name: String) = choice(name caseTo struct())
infix fun Type.giving(type: Type): Type = ArrowType(Arrow(this, type))

infix fun String.fieldTo(type: Type) = Field(this, type)
infix fun String.caseTo(type: Type) = Case(this, type)
val String.field get() = this fieldTo struct()
val String.case get() = this caseTo struct()
val Type.structOrNull: Struct? get() = (this as? StructType)?.struct
val Type.arrowOrNull: Arrow? get() = (this as? ArrowType)?.arrow
val Type.choiceOrNull: Choice? get() = (this as? ChoiceType)?.choice
val Struct.contentOrNull: Type? get() = fieldStack.onlyOrNull?.type
fun Struct.plus(field: Field) = Struct(fieldStack.push(field))
fun Choice.plus(case: Case) = Choice(caseStack.push(case))

fun Type.plus(field: Field) = StructType(structOrNull!!.plus(field))
fun Type.plus(case: Case) = ChoiceType(choiceOrNull!!.plus(case))

fun Type.getOrNull(name: String) =
	structOrNull
		?.contentOrNull
		?.structOrNull
		?.fieldStack
		?.first { it.name == name }
		?.let { struct(it) }

val Case.field get() = name fieldTo type