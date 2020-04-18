package leo15.core

import leo.base.SeqNode
import leo.base.then
import leo14.invoke
import leo15.*
import leo15.lambda.Term

val <T : Leo<T>> Typ<T>.linkTyp: Typ<Link<T>>
	get() =
		Typ(listName(ofName(scriptLine))) { Link(this@linkTyp, this) }

data class Link<T : Leo<T>>(val itemTyp: Typ<T>, override val term: Term) : Leo<Link<T>>() {
	override val typ get() = itemTyp.linkTyp
	override val scriptLine get() = linkName(tailName(tail.scriptLine), headName(head.scriptLine))
	val headAndTail: And<List<T>, T> get() = term.leo(itemTyp.listTyp.and(itemTyp))
	val tail: List<T> get() = headAndTail.first
	val head: T get() = headAndTail.second
}

infix fun <T : Leo<T>> List<T>.linkTo(tail: T): Link<T> = and(tail).term.leo(tail.typ.linkTyp)
val <T : Leo<T>> Link<T>.seqNode: SeqNode<T> get() = head then tail.seq