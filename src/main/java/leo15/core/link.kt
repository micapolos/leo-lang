package leo15.core

import leo.base.SeqNode
import leo.base.then
import leo14.invoke
import leo15.lambda.Term
import leo15.linkName
import leo15.ofName

val <T : Leo<T>> Typ<T>.linkTyp: Typ<Link<T>>
	get() =
		Typ(linkName(ofName(scriptLine))) { Link(this@linkTyp, this) }

data class Link<T : Leo<T>>(val itemTyp: Typ<T>, override val term: Term) : Leo<Link<T>>() {
	override val typ get() = itemTyp.linkTyp
	private val headAndTail: And<List<T>, T> get() = term of itemTyp.listTyp.and(itemTyp)
	override val unsafeScript get() = TODO()
	val tail: List<T> get() = headAndTail.first
	val head: T get() = headAndTail.second
	val unsafeTail: List<T> get() = headAndTail.unsafeFirst
	val unsafeHead: T get() = headAndTail.unsafeSecond
}

infix fun <T : Leo<T>> List<T>.linkTo(tail: T): Link<T> = and(tail).term.of(tail.typ.linkTyp)
val <T : Leo<T>> Link<T>.unsafeSeqNode: SeqNode<T> get() = unsafeHead then unsafeTail.unsafeSeq