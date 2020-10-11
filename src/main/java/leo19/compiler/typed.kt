package leo19.compiler

import leo13.EmptyStack
import leo13.LinkStack
import leo13.push
import leo19.term.ArrayTerm
import leo19.term.Term
import leo19.term.term
import leo19.type.Type
import leo19.type.fieldTo
import leo19.type.plus
import leo19.type.struct
import leo19.type.structOrNull

data class Typed(val term: Term, val type: Type)
data class TypedField(val name: String, val typed: Typed)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun String.fieldTo(typed: Typed) = TypedField(this, typed)
val TypedField.typeField get() = name fieldTo typed.type

val emptyTyped = Typed(term(), struct())

fun Typed.plus(field: TypedField) =
	type.structOrNull!!.let { struct ->
		when (struct.fieldStack) {
			is EmptyStack -> field.typed.term
			is LinkStack -> when (struct.fieldStack.link.stack) {
				is EmptyStack -> term(term, field.typed.term)
				is LinkStack -> ArrayTerm((term as ArrayTerm).stack.push(field.typed.term))
			}
		}.of(type.plus(field.typeField))
	}

fun typed(vararg fields: TypedField): Typed =
	when (fields.size) {
		1 -> fields[0].typed.term
		else -> term(*fields.map { it.typed.term }.toTypedArray())
	} of struct(*fields.map { it.typeField }.toTypedArray())
