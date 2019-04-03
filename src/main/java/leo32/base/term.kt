package leo32.base

import leo.base.appendableString
import leo.base.fold
import leo.base.ifNotNull

sealed class Term

data class SimpleTerm(
	val name: String): Term() {
	override fun toString() = appendableString { it.append(this) }
}

data class ComplexTerm(
	val lhs: Term,
	val field: Field): Term() {
	override fun toString() = appendableString { it.append(this) }
}

data class Field(
	val key: String,
	val value: Term?) {
	override fun toString() = appendableString { it.append(this) }
}

data class Struct(
	val name: String,
	val firstStructField: StructField,
	val remainingStructFields: StructFields)

data class StructFields(
	val firstStructField: StructField,
	val remainingStructFields: StructFields?) {
	override fun toString() = appendableString { it.append(this) }
}

data class StructField(
	val key: String,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

fun term(name: String, vararg names: String) =
	(SimpleTerm(name) as Term).fold(names) { term(it.field) }

fun Term.term(field: Field) =
	ComplexTerm(this, field) as Term

val Term.structOrNull: Struct? get() =
	when (this) {
		is SimpleTerm -> null
		is ComplexTerm -> if (complexTerm.field.value == null)
	}

val Term.structFieldsOrNull: StructFields? get() =
	when (this) {
		is SimpleTerm -> null
		is ComplexTerm ->
			if (field.value == null) StructFields(StructField(field.key, lhs), null)
			else lhs.structFieldsOrNull?.let {
				StructFields(StructField(field.key, field.value), it)
			}
	}

infix fun String.fieldTo(value: Term) =
	Field(this, value)

val String.field get() =
	Field(this, null)

fun Appendable.append(field: Field): Appendable =
	this
		.append(field.key)
		.ifNotNull(field.value) { append('(').append(it).append(')') }

fun Appendable.append(term: Term): Appendable =
	when (term) {
		is SimpleTerm -> append(term)
		is ComplexTerm -> append(term)
	}

fun Appendable.append(simpleTerm: SimpleTerm): Appendable =
	append(simpleTerm.name)

fun Appendable.append(complexTerm: ComplexTerm): Appendable =
	if (complexTerm.field.value == null) complexTerm.lhs.structOrNull?.let {
		append(complexTerm.field.key).append('(').append(it).append(')')
	} ?: appendNonStruct(complexTerm)
	else appendNonStruct(complexTerm)

fun Appendable.appendNonStruct(complexTerm: ComplexTerm): Appendable =
	append(complexTerm.lhs).append('.').append(complexTerm.field)

fun Appendable.append(struct: Struct): Appendable =
	append(struct.name)
		.append('(')
		.append(struct.firstStructField)
		.append(", ")
		.append(struct.remainingStructFields).append(")")

fun Appendable.append(structField: StructField): Appendable =
	append(structField.key).append("(").append(structField.value).append(")")

fun Appendable.append(struct: StructFields): Appendable =
	append(struct.firstStructField).ifNotNull(struct.remainingStructFields) {
		append(", ").append(it)
	}