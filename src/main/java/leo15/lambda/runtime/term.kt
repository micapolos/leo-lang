package leo15.lambda.runtime

import leo.base.*
import leo15.string

data class Term<out T>(val atom: Atom<T>, val applicationOrNull: Application<T>?) {
	override fun toString() = anyScript.string
}

data class Application<out T>(val term: Term<T>, val applicationOrNull: Application<T>?)

sealed class Atom<out T>
data class IndexAtom<T>(val index: Int) : Atom<T>()
data class ValueAtom<T>(val value: T) : Atom<T>()
data class TermAtom<T>(val term: Term<T>) : Atom<T>()

fun <T> atom(index: Int): Atom<T> = IndexAtom(index)
fun <T> atom(value: T): Atom<T> = ValueAtom(value)
fun <T> atom(term: Term<T>): Atom<T> = TermAtom(term)

fun <T> term(atom: Atom<T>, applicationOrNull: Application<T>?) = Term(atom, applicationOrNull)
fun <T> application(term: Term<T>, applicationOrNull: Application<T>?) = Application(term, applicationOrNull)

val <T> Atom<T>.value get() = (this as ValueAtom).value

fun <T> term(atom: Atom<T>, vararg terms: Term<T>): Term<T> =
	terms
		.foldRight(null as Application<T>?) { term, application -> application(term, application) }
		.let { application -> term(atom, application) }

val <T> Application<T>?.termSeq: Seq<Term<T>>
	get() =
		seq { this?.termSeqNode }

val <T> Application<T>.termSeqNode: SeqNode<Term<T>>
	get() =
		term then applicationOrNull.termSeq

fun <T> Application<T>?.join(applicationOrNull: Application<T>?): Application<T>? =
	applicationOrNull.fold(termSeq.reverse) { application(it, this) }
