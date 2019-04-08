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

val TermField.simpleNameOrNull get() =
	notNullIf(value.isEmpty) { name }

fun TermField.atOrNull(string: String) =
	notNullIf(name == string) { value }

fun TermField.map(fn: Term.() -> Term): TermField =
	name to value.fn()

const val termSeparatorChar = ' '
val termSeparatorSeq32 = seq(termSeparatorChar.i32)

val String.nameSeq32 get() =
	seq32
		.map {
			when (this) {
				termSeparatorChar.i32 -> seq('\\'.i32, '_'.i32)
				'\\'.i32 -> seq('\\'.i32, '\\'.i32)
				else -> onlySeq
			}
		}
		.flat

val TermField.seq32: Seq32 get() =
	name.nameSeq32.then {
		termSeparatorSeq32.then {
			value.seq32.then {
				termSeparatorSeq32
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

val Line.field: TermField get() =
	name to value.term

val TermField.line get() =
	Line(name, value.script)

fun termField(boolean: Boolean) =
	"boolean" to term("$boolean")

fun termField(int: Int) =
	"int" to term("$int")

val TermField.intOrNull get() =
	atOrNull("int")?.simpleNameOrNull?.toIntOrNull()
