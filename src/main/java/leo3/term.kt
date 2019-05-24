package leo3

import leo.base.Seq
import leo.base.flatSeq
import leo.binary.Bit

data class Term(
	val lhs: Value,
	val word: Word,
	val rhs: Value)

fun term(lhs: Value, word: Word, rhs: Value) =
	Term(lhs, word, rhs)

fun Term.plus(line: Line) =
	term(value(this), line.word, line.value)

val Term.bitSeq: Seq<Bit>
	get() = flatSeq(
		lhs.bitSeq,
		token(begin(word)).bitSeq,
		rhs.bitSeq,
		token(end).bitSeq)

fun Appendable.append(term: Term): Appendable =
	this
		.append(term.lhs)
		.append('(')
		.append(term.rhs)
		.append(')')

fun Writer.writePattern(term: Term) =
	writePattern(term.lhs).write(term.word).writePattern(term.rhs)