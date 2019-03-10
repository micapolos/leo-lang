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
	first.thenSeqNode(sequence(*remaining))

val <T> T.onlySeqNode: SeqNode<T>
	get() =
		SeqNode(this, emptySeq())

val <T> T.onlySeq: Seq<T>
	get() =
		Seq { onlySeqNode }

fun <T> sequence(vararg items: T): Seq<T> =
	sequenceFrom(0, listOf(*items))

fun <T> sequenceFrom(index: Int, items: List<T>): Seq<T> =
	Seq {
		if (index == items.size) null
		else items[index].thenSeqNode(sequenceFrom(index + 1, items))
	}

fun <T> Seq<T>.then(fn: () -> Seq<T>): Seq<T> =
	Seq { seqNodeOrNull ?: fn().seqNodeOrNull }