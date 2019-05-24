package leo3

import leo.base.*
import leo.binary.Bit

sealed class Value
data class EmptyValue(val empty: Empty) : Value()
data class TermValue(val term: Term) : Value()
data class FunctionValue(val function: Function) : Value()

fun value(empty: Empty): Value = EmptyValue(empty)
fun value(term: Term): Value = TermValue(term)
fun value(function: Function): Value = FunctionValue(function)

val Value.termOrNull get() = (this as? TermValue)?.term
val Value.emptyOrNull get() = (this as? EmptyValue)?.empty
val Value.functionOrNull get() = (this as? FunctionValue)?.function

fun value(vararg lines: Line) =
	value(empty).fold(lines) { plus(it) }

fun value(string: String) =
	value(line(word(string)))

fun Value.plus(line: Line) =
	value(term(this, line.word, line.value))

fun Appendable.append(value: Value): Appendable =
	when (value) {
		is EmptyValue -> this
		is TermValue -> append(value.term)
		is FunctionValue -> append(value.function)
	}

fun Value.apply(parameter: Parameter) = this

fun Value.call(line: Line): Value = TODO()
//	functionOrNull!!.call(value.termOrNull!!)

val Value.bitSeq: Seq<Bit>
	get() =
		when (this) {
			is EmptyValue -> emptySeq()
			is TermValue -> term.bitSeq
			is FunctionValue -> function.bitSeq
		}

fun Writer.writePattern(value: Value): Writer =
	when (value) {
		is EmptyValue -> write0
		is TermValue -> write1.write0.writePattern(value.term)
		is FunctionValue -> write1.write1.writePattern(value.function)
	}

fun valueOrNull(bitSeq: Seq<Bit>): Value? =
	value(empty).completedBitReader.orNullFold(bitSeq) { read(it) }?.valueOrNull
