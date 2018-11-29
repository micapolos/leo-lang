package leo.base

data class Stack<out V>(
	val pop: Stack<V>?,
	val top: V) {
	override fun toString() = appendableString { it.append(this) }
}

val <V> V.onlyStack
	get() =
		Stack(null, this)

//fun <V> Stack<V>.updateTopOrNull(fn: (V) -> V): Stack<V> =
//	pop?.push(fn(top))

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
		top.onlyStack.fold(pop, Stack<V>::push)

val <V : Any> Stack<V>.onlyOrNull: V?
	get() =
		if (pop == null) top else null

fun <V : Any> Stack<V>.theOnlyOrNull(fn: (V) -> Boolean): V? =
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
	else fn(stack.top)?.foldOrNull(stack.pop, fn)

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
		index == 0 -> top
		pop == null -> null
		else -> pop[index - 1]
	}

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
		top.onlyStream.then { pop?.stream }

fun <V> Int.sizeStackOrNullOf(value: V): Stack<V>? =
	nullOf<Stack<V>>().iterate(this) { push(value) }