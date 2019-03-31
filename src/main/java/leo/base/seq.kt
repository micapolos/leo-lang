package leo.base

data class SeqNode<T>(
	val first: T,
	val remaining: Seq<T>) : Iterable<T> {
	override fun iterator() = seq.iterator()
}

data class Seq<T>(
	val seqNodeOrNullFn: () -> SeqNode<T>?) : Iterable<T> {
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
				nextSeqNodeOrNull = seqOrEmptyOrNull!!.seqNodeOrNull
			}
		}
	}
}

val <T> Seq<T>.seqNodeOrNull: SeqNode<T>?
	get() =
		seqNodeOrNullFn()

fun <T> emptySeq(): Seq<T> =
	Seq { null }

val <T> SeqNode<T>.seq: Seq<T>
	get() =
		Seq { this }

fun <T> T.thenSeqNode(seq: Seq<T>): SeqNode<T> =
	SeqNode(this, seq)

fun <T> seqNode(first: T, vararg remaining: T): SeqNode<T> =
	first.thenSeqNode(seq(*remaining))

val <T> T.onlySeqNode: SeqNode<T>
	get() =
		SeqNode(this, emptySeq())

val <T> T.onlySeq: Seq<T>
	get() =
		Seq { onlySeqNode }

fun <T> seq(vararg items: T): Seq<T> =
	seqFrom(0, listOf(*items))

fun <T> flatSeq(vararg seqs: Seq<T>): Seq<T> =
	seq(*seqs).flat

fun <T> seqFrom(index: Int, items: List<T>): Seq<T> =
	Seq {
		if (index == items.size) null
		else items[index].thenSeqNode(seqFrom(index + 1, items))
	}

fun <T> Seq<T>.then(fn: () -> Seq<T>): Seq<T> =
	Seq {
		seqNodeOrNull.let { seqNodeOrNull ->
			seqNodeOrNull
				?.first
				?.thenSeqNode(seqNodeOrNull.remaining.then(fn))
				?: fn().seqNodeOrNull
		}
	}

fun <T, R> R.fold(seq: Seq<T>, fn: R.(T) -> R) =
	seq.fold(this, fn)

fun <T, R> SeqNode<T>.map(fn: T.() -> R): SeqNode<R> =
	first.fn().thenSeqNode(remaining.map(fn))

fun <T, R> Seq<T>.map(fn: T.() -> R): Seq<R> =
	Seq { seqNodeOrNull?.map(fn) }

val <T> SeqNode<Seq<T>>.flatten: Seq<T>
	get() =
		first.then { remaining.flat }

val <T> Seq<Seq<T>>.flat: Seq<T>
	get() =
		Seq { seqNodeOrNull?.flatten?.seqNodeOrNull }

val <T> Iterator<T>.seq: Seq<T>
	get() =
		Seq {
			if (hasNext()) next().thenSeqNode(seq)
			else null
		}

val <T> Iterable<T>.seq: Seq<T>
	get() =
		iterator().seq

fun <T> Seq<T>.prepend(value: T) =
	Seq {
		value.thenSeqNode(this)
	}

fun <T> Seq<T>.prepend(seq: Seq<T>) =
	seq.then(this)
