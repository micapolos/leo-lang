package leo32.runtime

import leo.base.appendableString

data class TermField(
	val name: String,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun String.to(term: Term) =
	TermField(this, term)

fun Appendable.append(field: TermField): Appendable =
	append(field.name).run {
		if (!field.value.isEmpty) append('(').append(field.value).append(')')
		else this
	}

fun TermField.map(fn: Term.() -> Term): TermField =
	name to value.fn()