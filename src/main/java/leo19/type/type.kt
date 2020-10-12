package leo19.type

import leo.base.fold
import leo.base.indexed
import leo.base.nullOf
import leo.base.reverse
import leo.base.runIf
import leo13.Stack
import leo13.first
import leo13.onlyOrNull
import leo13.push
import leo13.seq
import leo13.stack

sealed class Type
data class StructType(val struct: Struct) : Type()
data class ChoiceType(val choice: Choice) : Type()
data class ArrowType(val arrow: Arrow) : Type()

data class Struct(val fieldStack: Stack<Field>)
data class Choice(val caseStack: Stack<Case>)

data class Field(val name: String, val type: Type)
data class Case(val name: String, val type: Type)
data class Arrow(val lhs: Type, val rhs: Type)

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

fun Struct.indexedFieldOrNull(name: String): IndexedValue<Field>? =
	0.indexed(nullOf<IndexedValue<Field>>()).fold(fieldStack.seq.reverse) { field ->
		index
			.runIf(!field.isStatic) { inc() }
			.indexed(if (field.name == name) index.indexed(field) else value)
	}.value

fun Type.indexedOrNull(name: String): IndexedValue<Type>? =
	structOrNull
		?.contentOrNull
		?.structOrNull
		?.indexedFieldOrNull(name)
		?.run { index indexed struct(value) }

val Case.field get() = name fieldTo type