package leo32.term

import leo.base.appendableString
import leo.base.empty

data class TermField(
	val name: String,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun String.fieldTo(term: Term) =
	TermField(this, term)

val String.termField get() =
	this fieldTo empty.term

fun Appendable.append(field: TermField): Appendable =
	append(field.name).run {
		if (!field.value.isEmpty) append('(').append(field.value).append(')')
		else this
	}