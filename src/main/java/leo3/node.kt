package leo3

import leo.base.*

data class Node(
	val lhs: Node?,
	val word: Word,
	val rhs: Node?)

fun term(lhs: Node?, word: Word, rhs: Node?) =
	Node(lhs, word, rhs)

fun Node?.plus(word: Word, rhs: Node? = null) =
	term(this, word, rhs)

val Node.tokenSeqNode: SeqNode<Token>?
	get() = seqNodeOrNull(
		lhs.tokenSeq,
		token(begin(word)).onlySeq,
		rhs.tokenSeq,
		token(end).onlySeq)

val Node?.tokenSeq: Seq<Token>
	get() = Seq { this?.tokenSeqNode }

fun Appendable.append(node: Node): Appendable =
	fold(node.tokenSeq) { append(it) }
