package leo32.runtime

import leo.base.*
import leo.binary.Bit
import leo.binary.bitSeq
import leo32.Seq32
import leo32.base.i32
import leo32.seq32

data class TermField(
	val name: Symbol,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun Symbol.to(term: Term) =
	TermField(this, term)

infix fun String.to(term: Term) =
	symbol(this) to term

val TermField.simpleNameOrNull get() =
	notNullIf(value.isEmpty) { name }

fun TermField.atOrNull(symbol: Symbol) =
	notNullIf(name == symbol(string)) { value }

fun TermField.map(fn: Term.() -> Term): TermField =
	name to value.fn()

const val termBeginChar = '('
const val termEndChar = ')'
const val termEscapeChar = '\\'

val String.nameSeq32 get() =
	seq32
		.map {
			when (this) {
				termBeginChar.i32 -> seq(termEscapeChar.i32, termBeginChar.i32)
				termEndChar.i32 -> seq(termEscapeChar.i32, termEndChar.i32)
				termEscapeChar.i32 -> seq(termEscapeChar.i32, termEscapeChar.i32)
				else -> onlySeq
			}
		}
		.flat

val TermField.seq32: Seq32 get() =
	name.string.nameSeq32.then {
		termBeginChar.i32.onlySeq.then {
			value.seq32.then {
				termEndChar.i32.onlySeq
			}
		}
	}

val TermField.bitSeq: Seq<Bit>
	get() =
		name.bitSeq.then {
			termBeginChar.toByte().bitSeq.then {
				value.bitSeq.then {
					termEndChar.toByte().bitSeq
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
	atOrNull(intSymbol)?.simpleNameOrNull?.string?.toIntOrNull()

fun TermField.leafPlus(term: Term) =
	name to value.leafPlus(term)

val TermField.typeTermField get() =
	name to value.typeTerm
