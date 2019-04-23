package leo32.runtime

import leo.base.*
import leo.binary.Bit
import leo.binary.byteBitSeq

data class Field(
	val name: Symbol,
	val value: Term) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun Symbol.to(term: Term) =
	Field(this, term)

infix fun String.to(term: Term) =
	symbol(this) to term

val Field.simpleNameOrNull
	get() =
	notNullIf(value.isEmpty) { name }

fun Field.atOrNull(symbol: Symbol) =
	notNullIf(name == symbol) { value }

fun Field.map(fn: Term.() -> Term): Field =
	name to value.fn()

val Field.bitSeq: Seq<Bit>
	get() =
		byteSeq.byteBitSeq

val Field.byteSeq: Seq<Byte>
	get() =
		name.byteSeq.then {
			value.byteSeq.then {
				0.toByte().onlySeq
			}
		}

fun Appendable.append(field: Field): Appendable =
	append(field.name).let {
		if (field.value.isEmpty) this
		else this
			.tryAppend { append(' ').appendSimple(field.value) }
			?: append('(').append(field.value).append(')')
	}

val Line.field: Field
	get() =
	name to value.term

val Field.line
	get() =
	Line(name, value.script)

fun termField(boolean: Boolean) =
	booleanSymbol to term("$boolean")

fun termField(int: Int) =
	intSymbol to term("$int")

fun Field.leafPlus(term: Term) =
	name to value.leafPlus(term)

val Field.lineField
	get() =
		lineSymbol to term(
			name.stringField,
			value.scriptField)
