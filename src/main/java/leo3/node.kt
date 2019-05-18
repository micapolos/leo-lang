package leo3

import leo.base.*
import leo.binary.Bit

data class Node(
	val lhs: Value,
	val word: Word,
	val rhs: Value)

fun node(lhs: Value, word: Word, rhs: Value) =
	Node(lhs, word, rhs)

fun Value.plus(word: Word, rhs: Value) =
	node(this, word, rhs)

val Node.tokenSeq: Seq<Token>
	get() = flatSeq(
		lhs.tokenSeq,
		token(begin(word)).onlySeq,
		rhs.tokenSeq,
		token(end).onlySeq)

val Node.bitSeq: Seq<Bit>
	get() = tokenSeq.map { bitSeq }.flat

fun Appendable.append(node: Node): Appendable =
	fold(node.tokenSeq) { append(it) }
