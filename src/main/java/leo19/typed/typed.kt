package leo19.typed

import leo19.term.Term
import leo19.term.get
import leo19.term.nullTerm
import leo19.term.plus
import leo19.term.term
import leo19.type.Type
import leo19.type.contentOrNull
import leo19.type.fieldTo
import leo19.type.indexedOrNull
import leo19.type.isSingle
import leo19.type.isStatic
import leo19.type.plus
import leo19.type.struct
import leo19.type.structOrNull

data class Typed(val term: Term, val type: Type)
data class TypedField(val name: String, val typed: Typed)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun String.fieldTo(typed: Typed) = TypedField(this, typed)
val TypedField.typeField get() = name fieldTo typed.type

val nullTyped = Typed(nullTerm, struct())

fun Typed.plus(field: TypedField): Typed =
	type.structOrNull!!.let { struct ->
		if (struct.isStatic)
			if (field.typed.type.isStatic) nullTerm
			else field.typed.term
		else
			if (field.typed.type.isStatic) term
			else term.plus(field.typed.term)
	} of type.plus(field.typeField)

fun Typed.getOrNull(name: String): Typed? =
	type.structOrNull?.contentOrNull?.structOrNull?.let { struct ->
		type.indexedOrNull(name)?.let { indexedField ->
			when {
				struct.isStatic -> nullTerm
				struct.isSingle -> if (indexedField.value.isStatic) nullTerm else term
				else -> term.get(term(indexedField.index))
			}.of(indexedField.value)
		}
	}
