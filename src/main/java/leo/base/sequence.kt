package leo.base

data class Sequence<T>(
	val nonEmptySequenceOrNullFn: () -> NonEmptySequence<T>?) : Iterable<T> {
	override fun iterator() = object : Iterator<T> {
		var sequenceOrNull: Sequence<T>? = this@Sequence
		var nextNonEmptySequenceOrNull: NonEmptySequence<T>? = null

		override fun hasNext(): Boolean {
			prepareNext()
			return nextNonEmptySequenceOrNull != null
		}

		override fun next(): T {
			prepareNext()
			val nextNonEmptySequence = nextNonEmptySequenceOrNull!!
			sequenceOrNull = nextNonEmptySequence.remaining
			nextNonEmptySequenceOrNull = null
			return nextNonEmptySequence.first
		}

		private fun prepareNext() {
			if (nextNonEmptySequenceOrNull == null && sequenceOrNull != null) {
				nextNonEmptySequenceOrNull = sequenceOrNull!!.nonEmptySequenceOrNull
			}
		}
	}
}

data class NonEmptySequence<T>(
	val first: T,
	val remaining: Sequence<T>) : Iterable<T> {
	override fun iterator() = sequence.iterator()
}

val <T> Sequence<T>.nonEmptySequenceOrNull: NonEmptySequence<T>?
	get() =
		nonEmptySequenceOrNullFn()

fun <T> emptySequence(): Sequence<T> =
	Sequence { null }

val <T> NonEmptySequence<T>.sequence: Sequence<T>
	get() =
		Sequence { this }

fun <T> T.thenNonEmptySequence(sequence: Sequence<T>): NonEmptySequence<T> =
	NonEmptySequence(this, sequence)

val <T> T.onlyNonEmptySequence
	get() =
		this.thenNonEmptySequence(emptySequence())

fun <T> nonEmptySequence(first: T, vararg remaining: T) =
	first.thenNonEmptySequence(sequence(*remaining))

val <T> T.onlySequence: Sequence<T>
	get() =
		onlyNonEmptySequence.sequence

fun <T> sequence(vararg items: T): Sequence<T> =
	sequenceFrom(0, listOf(*items))

fun <T> sequenceFrom(index: Int, items: List<T>): Sequence<T> =
	Sequence {
		if (index == items.size) null
		else items[index].thenNonEmptySequence(sequenceFrom(index + 1, items))
	}
