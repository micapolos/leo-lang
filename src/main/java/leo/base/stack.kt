package leo.base

data class Stack<out V>(
	val tail: Stack<V>?,
	val head: V) {
	override fun toString() = appendableString { it.append(this) }
}

val <V> V.onlyStack
	get() =
		Stack(null, this)

fun <V> Stack<V>?.push(top: V): Stack<V> =
	Stack(this, top)

fun <V> Stack<V>?.push(value: V, vararg values: V) =
	values.fold(push(value)) { stack, nextValue ->
		stack.push(nextValue)
	}

fun <V> stack(value: V, vararg values: V): Stack<V> =
	values.fold(value.onlyStack) { stack, top ->
		stack.push(top)
	}

fun <V> stackOrNull(vararg values: V): Stack<V>? =
	values.fold(nullOf()) { stack, top ->
		stack.push(top)
	}

fun <V, R> R.fold(stack: Stack<V>?, fn: R.(V) -> R): R =
	fold(stack?.stream, fn)

val <V> Stack<V>.reverse: Stack<V>
	get() =
		head.onlyStack.fold(tail, Stack<V>::push)

val <V : Any> Stack<V>.onlyOrNull: V?
	get() =
		if (tail == null) head else null

fun <V> Stack<V>.pop(int: Int): Stack<V>? =
	orNull.iterate(int) { this?.tail }

fun <V : Any> Stack<V>.onlyOrNull(fn: (V) -> Boolean): V? =
	all(fn)?.onlyOrNull

fun <V : Any> Stack<V>.all(fn: (V) -> Boolean): Stack<V>? =
	stream.all(fn)?.stack?.reverse

fun <V, R> Stack<V>.map(fn: (V) -> R): Stack<R> =
	stream.map(fn).stack.reverse

fun <V, R> Stack<V>.mapOrNull(fn: (V) -> R?): Stack<R>? =
	nullOf<Stack<R>>().the.foldOrNull(this) { value ->
		fn(value)?.let { this.value.push(it).the }
	}?.value?.reverse

fun <V, R : Any> R.foldOrNull(stack: Stack<V>?, fn: R.(V) -> R?): R? =
	if (stack == null) this
	else fn(stack.head)?.foldOrNull(stack.tail, fn)

fun <V, R : Any> Stack<V>.filterMap(fn: (V) -> R?): Stack<R>? =
	stream.filterMap(fn)?.stack?.reverse

fun <V : Any> Stack<V>.top(fn: (V) -> Boolean): V? =
	stream.first(fn)

fun <V> Stack<V>.contains(value: V): Boolean =
	stream.contains(value)

val Stack<*>.sizeInt: Int
	get() =
		0.fold(this) { this + 1 }

val Stack<*>.indexBitCount: Int
	get() =
		32 - Integer.numberOfLeadingZeros(sizeInt - 1)

tailrec operator fun <V : Any> Stack<V>.get(index: Int): V? =
	when {
		index == 0 -> head
		tail == null -> null
		else -> tail[index - 1]
	}

fun <V> Stack<V>.split(acc: Pair<Stack<V>?, Stack<V>?>, predicate: (V) -> Boolean): Pair<Stack<V>?, Stack<V>?> =
	when (predicate(head)) {
		false -> acc.first.push(head) to acc.second
		true -> acc.first to acc.second.push(head)
	}.let { tailAcc ->
		tail?.split(tailAcc, predicate) ?: tailAcc
	}

fun <V> Stack<V>.split(predicate: (V) -> Boolean): Pair<Stack<V>?, Stack<V>?> =
	split(null to null, predicate)

fun <V> Stack<V>.removeTop(value: V): Stack<V>? =
	if (head == value) tail
	else tail.removeTop(value).push(head)

// TODO: Using clampedInt is "cheating", implement it properly some day
operator fun <V : Any> Stack<V>.get(binary: Binary): V? =
	this[binary.clampedInt]

// === appendable

fun Appendable.append(stack: Stack<*>) =
	(append("stack(") to true)
		.fold(stack.reverse) { value ->
			val (appendable, isFirst) = this
			if (isFirst) appendString(value) to false
			else appendable.append(", ").appendString(value) to false
		}
		.first
		.append(")")

val <V> Stack<V>.stream: Stream<V>
	get() =
		head.onlyStream.then { tail?.stream }

fun <V> Int.sizeStackOrNullOf(value: V): Stack<V>? =
	nullOf<Stack<V>>().iterate(this) { push(value) }

// === processor

val <V> Stack<V>.pushProcessor: Processor<Stack<V>, V>
	get() =
		Processor(this) { value ->
			push(value).pushProcessor
		}

val <V> Stack<V>?.seq: Seq<V>
	get() =
		Seq {
			ifNotNull { stack ->
				stack.head.then(stack.tail.seq)
			}
		}
