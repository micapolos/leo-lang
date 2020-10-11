package leo19.compiler

import leo19.term.Term
import leo19.term.term
import leo19.type.Type
import leo19.type.struct
import leo19.type.fieldTo

data class Typed(val term: Term, val type: Type)
data class TypedField(val name: String, val typed: Typed)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun String.fieldTo(typed: Typed) = TypedField(this, typed)
val TypedField.typeField get() = name fieldTo typed.type

fun typed(vararg fields: TypedField): Typed =
	when (fields.size) {
		1 -> fields[0].typed.term
		else -> term(*fields.map { it.typed.term }.toTypedArray())
	} of struct(*fields.map { it.typeField }.toTypedArray())
