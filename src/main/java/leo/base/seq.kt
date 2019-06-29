@file:Suppress("unused")

package leo.base

import leo32.base.Effect
import leo32.base.effect
import leo32.base.mapValue

data class SeqNode<T>(
	val first: T,
	val remaining: Seq<T>) : Iterable<T> {
	override fun iterator() = seq.iterator()
}

data class Seq<T>(
	val nodeOrNullFn: () -> SeqNode<T>?) : Iterable<T> {
	override fun iterator() = object : Iterator<T> {
		var seqOrEmptyOrNull: Seq<T>? = this@Seq
		var nextSeqNodeOrNull: SeqNode<T>? = null

		override fun hasNext(): Boolean {
			prepareNext()
			return nextSeqNodeOrNull != null
		}

		override fun next(): T {
			prepareNext()
			val nextNonEmptySequence = nextSeqNodeOrNull!!
			seqOrEmptyOrNull = nextNonEmptySequence.remaining
			nextSeqNodeOrNull = null
			return nextNonEmptySequence.first
		}

		private fun prepareNext() {
			if (nextSeqNodeOrNull == null && seqOrEmptyOrNull != null) {
				nextSeqNodeOrNull = seqOrEmptyOrNull!!.nodeOrNull
			}
		}
	}
}

val <T> Seq<T>.nodeOrNull: SeqNode<T>?
	get() =
		nodeOrNullFn()

fun <T> Empty.seq() =
	emptySeq<T>()

fun <T> emptySeq(): Seq<T> =
	Seq { null }

fun <T> seq(fn: () -> SeqNode<T>?) =
	Seq(fn)

val <T> SeqNode<T>.seq: Seq<T>
	get() =
		Seq { this }

infix fun <T> T.then(seq: Seq<T>): SeqNode<T> =
	SeqNode(this, seq)

fun <T> seqNode(first: T, vararg remaining: T): SeqNode<T> =
	first then seq(*remaining)

val <T> T.onlySeqNode: SeqNode<T>
	get() =
		SeqNode(this, emptySeq())

fun <T> seqNode(value: T): SeqNode<T> =
	value.onlySeqNode

val <T> T.onlySeq: Seq<T>
	get() =
		Seq { onlySeqNode }

fun <T : Any, R> T?.orEmptyIfNullSeq(fn: T.() -> Seq<R>): Seq<R> =
	if (this == null) seq() else fn()

fun <T> seq(vararg items: T): Seq<T> =
	seqFrom(0, listOf(*items))

fun <T> flatSeq(vararg seqs: Seq<T>): Seq<T> =
	seq(*seqs).flat

fun <T> seqNodeOrNull(vararg seqs: Seq<T>): SeqNode<T>? =
	flatSeq(*seqs).nodeOrNull

fun <T> seqFrom(index: Int, items: List<T>): Seq<T> =
	Seq {
		if (index == items.size) null
		else items[index] then seqFrom(index + 1, items)
	}

fun <T> Seq<T>.thenFn(fn: () -> Seq<T>): Seq<T> =
	Seq {
		nodeOrNull.let { seqNodeOrNull ->
			seqNodeOrNull
				?.first
				?.then(seqNodeOrNull.remaining.thenFn(fn))
				?: fn().nodeOrNull
		}
	}

fun <T> Seq<T>.then(seq: Seq<T>): Seq<T> =
	Seq {
		nodeOrNull.let { seqNodeOrNull ->
			seqNodeOrNull
				?.first
				?.then(seqNodeOrNull.remaining.then(seq))
				?: seq.nodeOrNull
		}
	}

fun <T, R> R.fold(seq: Seq<T>, fn: R.(T) -> R) =
	seq.fold(this, fn)

fun <T, R> R.applyEach(seq: Seq<T>, fn: R.(T) -> Unit): R =
	fold(seq) { apply { fn(it) } }

fun <T, R : Any> R?.orNullFold(seq: Seq<T>, fn: R.(T) -> R?): R? =
	orNull.fold(seq) { this?.fn(it) }

fun <T> Seq<T>.runAll(fn: T.() -> Unit) =
	Unit.fold(this) { it.fn() }

fun <T, R> SeqNode<T>.map(fn: T.() -> R): SeqNode<R> =
	first.fn() then remaining.map(fn)

fun <T, R> Seq<T>.map(fn: T.() -> R): Seq<R> =
	Seq { nodeOrNull?.map(fn) }

val <T> SeqNode<Seq<T>>.flat: Seq<T>
	get() =
		first.thenFn { remaining.flat }

val <T> Seq<Seq<T>>.flat: Seq<T>
	get() =
		Seq { nodeOrNull?.flat?.nodeOrNull }

fun <T, R> Seq<T>.mapFlat(fn: T.() -> Seq<R>) =
	map(fn).flat

val <T> Iterator<T>.seq: Seq<T>
	get() =
		Seq {
			if (hasNext()) next() then seq
			else null
		}

val <T> Iterable<T>.seq: Seq<T>
	get() =
		iterator().seq

fun <T> Seq<T>.prepend(value: T) =
	Seq {
		value then this
	}

fun <T> Seq<T>.prepend(seq: Seq<T>) =
	seq then this

fun <T> Seq<T>.intercept(value: T) =
	intercept(false, value)

val <T: Any> Seq<T>.nullIntercept: Seq<T?> get() =
	map { orNull }.intercept(null)


fun <T> Seq<T>.intercept(addValue: Boolean, value: T): Seq<T> =
	Seq { nodeOrNull?.intercept(addValue, value) }

fun <T> SeqNode<T>.intercept(addValue: Boolean, value: T): SeqNode<T> =
	if (!addValue) first.then(remaining.intercept(true, value))
	else value.then(Seq { intercept(false, value) })

fun <T> Seq<T>.replace(fromToPair: Pair<T, T>) =
	map { value -> if (value == fromToPair.first) fromToPair.second else value }

fun <T, V> Seq<T>.filterMap(fn: T.() -> The<V>?): Seq<V> {
	val seqNodeOrNull = nodeOrNull
	return if (seqNodeOrNull == null) Seq { null }
	else {
		val theMappedFirst = seqNodeOrNull.first.fn()
		if (theMappedFirst == null) seqNodeOrNull.remaining.filterMap(fn)
		else Seq { theMappedFirst.value.then(seqNodeOrNull.remaining.filterMap(fn)) }
	}
}

fun <T, R : Any> R.foldUntilNullEffect(seq: Seq<T>, fn: R.(T) -> R?): Effect<R, Seq<T>> =
	foldUntilNullEffect(seq.nodeOrNull, fn).mapValue { Seq { this } }

fun <T, R : Any> R.foldUntilNullEffect(seqNodeOrNull: SeqNode<T>?, fn: R.(T) -> R?): Effect<R, SeqNode<T>?> {
	var folded = this
	var remainingSeqNodeOrNull = seqNodeOrNull
	while (remainingSeqNodeOrNull != null) {
		val nextFolded = folded.fn(remainingSeqNodeOrNull.first)
		if (nextFolded == null) break
		else {
			folded = nextFolded
			remainingSeqNodeOrNull = remainingSeqNodeOrNull.remaining.nodeOrNull
		}
	}
	return folded.effect(remainingSeqNodeOrNull)
}

fun <A : Any, B : Any, R> R.zipFold(aSeq: Seq<A>, bSeq: Seq<B>, fn: R.(A?, B?) -> R): R =
	fold(zip(aSeq, bSeq)) { (a, b) -> fn(a, b) }

fun <A : Any, B : Any> zip(aSeq: Seq<A>, bSeq: Seq<B>): Seq<Pair<A?, B?>> =
	Seq { zip(aSeq.nodeOrNull, bSeq.nodeOrNull) }

fun <A : Any, B : Any> zip(aSeqNode: SeqNode<A>?, bSeqNode: SeqNode<B>?): SeqNode<Pair<A?, B?>>? =
	if (aSeqNode != null)
		if (bSeqNode != null) (aSeqNode.first to bSeqNode.first).then(zip(aSeqNode.remaining, bSeqNode.remaining))
		else (aSeqNode.first to nullOf<B>()).then(zip(aSeqNode.remaining, seq<B>()))
	else
		if (bSeqNode != null) (nullOf<A>() to bSeqNode.first).then(zip(seq<A>(), bSeqNode.remaining))
		else null

fun <V> repeatSeq(value: V, count: Int): Seq<V> =
	Seq { repeatSeqNodeOrNull(value, count) }

fun <V> repeatSeqNodeOrNull(value: V, count: Int): SeqNode<V>? =
	notNullIf(count != 0) {
		value.then(repeatSeq(value, count.dec()))
	}
