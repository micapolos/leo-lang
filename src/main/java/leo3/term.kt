package leo3

import leo.base.*

data class Term(
	val lhs: Term?,
	val word: Word,
	val rhs: Term?)

fun term(lhs: Term?, word: Word, rhs: Term?) =
	Term(lhs, word, rhs)

fun Term?.plus(word: Word, rhs: Term? = null) =
	term(this, word, rhs)

val Term.tokenSeqNode: SeqNode<Token>?
	get() = seqNodeOrNull(
		lhs.tokenSeq,
		token(begin(word)).onlySeq,
		rhs.tokenSeq,
		token(end).onlySeq)

val Term?.tokenSeq: Seq<Token>
	get() = Seq { this?.tokenSeqNode }

fun Appendable.append(term: Term): Appendable =
	fold(term.tokenSeq) { append(it) }
