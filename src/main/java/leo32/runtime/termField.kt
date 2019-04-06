package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.i32
import leo32.seq32

data class TermField(
	val name: String,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun String.to(term: Term) =
	TermField(this, term)

fun TermField.map(fn: Term.() -> Term): TermField =
	name to value.fn()

val separatorI32 = '.'.i32
val escapeI32 = '\\'.i32
val separatorSeq32 = seq(separatorI32)

val String.nameSeq32 get() =
	seq32
		.map {
			when (this) {
				separatorI32 -> seq(escapeI32, '_'.i32)
				escapeI32 -> seq(escapeI32, escapeI32)
				else -> onlySeq
			}
		}
		.flat

val TermField.seq32: Seq32 get() =
	name.nameSeq32.then {
		separatorSeq32.then {
			value.seq32.then {
				separatorSeq32
			}
		}
	}

fun Appendable.append(field: TermField): Appendable =
	append(field.name).let {
		if (field.value.isEmpty) this
		else this
			.tryAppend { append(' ').appendSimple(field.value) }
			?: append('(').append(field.value).append(')')
	}
