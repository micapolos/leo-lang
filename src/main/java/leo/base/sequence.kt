package leo.base

data class Sequence<T>(
	val nonEmptySequenceOrNullFn: () -> NonEmptySequence<T>?) : Iterable<T> {
	override fun iterator() = object : Iterator<T> {
		var isPrepared = false
		var nextNonEmptySequenceOrNull: NonEmptySequence<T>? = null

		override fun hasNext(): Boolean {
			prepareNext()
			return nextNonEmptySequenceOrNull != null
		}

		override fun next(): T {
			prepareNext()
			isPrepared = false
			return nextNonEmptySequenceOrNull!!.firstThen.first
		}

		private fun prepareNext() {
			if (!isPrepared) {
				nextNonEmptySequenceOrNull = nonEmptySequenceOrNullFn()
				isPrepared = true
			}
		}
	}
}

data class NonEmptySequence<T>(
	val firstThen: FirstThen<T, Sequence<T>>) : Iterable<T> {
	override fun iterator() = sequence.iterator()
}

fun <T> emptySequence(): Sequence<T> =
	Sequence { null }

val <T> NonEmptySequence<T>.sequence: Sequence<T>
	get() =
		Sequence { this }

val <T> T.onlyNonEmptySequence
	get() =
		NonEmptySequence(this.then(emptySequence()))

fun <T> T.thenNonEmptySequence(sequence: Sequence<T>): NonEmptySequence<T> =
	NonEmptySequence(this.then(sequence))

fun <T> nonEmptySequence(first: T, vararg remaining: T) =
	NonEmptySequence(first.then(sequence(*remaining)))

val <T> T.onlySequence: Sequence<T>
	get() =
		onlyNonEmptySequence.sequence

fun <T> sequence(vararg items: T): Sequence<T> =
	sequence(0, *items)

fun <T> sequence(index: Int, vararg items: T): Sequence<T> =
	Sequence {
		if (index == items.size) null
		else items[index].thenNonEmptySequence(sequence(index + 1, *items))
	}
