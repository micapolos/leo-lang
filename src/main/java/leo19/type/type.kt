package leo19.type

import leo.base.notNullIf
import leo13.Stack
import leo13.first
import leo13.onlyOrNull
import leo13.push
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

fun struct(vararg fields: Field) = Struct(stack(*fields))
infix fun Type.arrowTo(type: Type) = Arrow(this, type)

fun type(vararg fields: Field): Type = StructType(struct(*fields))
fun type(name: String) = type(name fieldTo type())
fun choice(vararg cases: Case): Type = ChoiceType(Choice(stack(*cases)))
fun choice(name: String) = choice(name caseTo type())
infix fun Type.giving(type: Type): Type = ArrowType(Arrow(this, type))

infix fun String.fieldTo(type: Type) = Field(this, type)
infix fun String.caseTo(type: Type) = Case(this, type)
val String.field get() = this fieldTo type()
val String.case get() = this caseTo type()
val Type.structOrNull: Struct? get() = (this as? StructType)?.struct
val Type.contentOrNull: Type? get() = structOrNull?.contentOrNull
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
		?.let { type(it) }

val Case.field get() = name fieldTo type

val Type.nameOrNull: String?
	get() =
		structOrNull?.fieldStack?.onlyOrNull?.nameOrNull

val Field.nameOrNull: String?
	get() =
		notNullIf(type == type()) { name }