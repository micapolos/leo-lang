package leo32.term

import leo.base.appendableString

data class Field(
	val name: String,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun String.fieldTo(term: Term) =
	Field(this, term)

fun Appendable.append(field: Field): Appendable =
	append(field.name).run {
		if (!field.value.isEmpty) append('(').append(field.value).append(')')
		else this
	}