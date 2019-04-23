package leo32.runtime

import leo.base.*
import leo.binary.Bit
import leo.binary.byteBitSeq

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
	notNullIf(name == symbol) { value }

fun TermField.map(fn: Term.() -> Term): TermField =
	name to value.fn()

const val termBeginChar = '('
const val termEndChar = ')'
const val termEscapeChar = '\\'

val TermField.bitSeq: Seq<Bit>
	get() =
		byteSeq.byteBitSeq

val TermField.byteSeq: Seq<Byte>
	get() =
		name.byteSeq.then {
			value.byteSeq.then {
				0.toByte().onlySeq
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

val TermField.lineField
	get() =
		lineSymbol to term(
			name.stringField,
			value.scriptField)

fun <R : Any> TermField.ifSimpleOrNull(symbol: Symbol, fn: () -> R): R? =
	simpleNameOrNull?.let { name ->
		ifOrNull(name == symbol, fn)
	}
