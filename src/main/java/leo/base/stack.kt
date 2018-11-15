package leo.base

import leo.*

data class Stack<out V>(
	val pop: Stack<V>?,
	val top: V) {
	override fun toString() = appendableString { it.append(this) }

	data class FoldedTop<V, R>(
		val foldedTop: R,
		val pop: Stack<V>?
	)
}

fun <V> nullStack() =
	null as Stack<V>?

val <V> V.onlyStack
	get() =
		Stack(null, this)

fun <V> Stack<V>.updateTopOrNull(fn: (V) -> V?): Stack<V>? =
	fn(top)?.let { copy(top = it) }

fun <V> Stack<V>?.push(top: V) =
	Stack(this, top)

fun <V> Stack<V>?.push(value: V, vararg values: V) =
	values.fold(push(value)) { stack, nextValue ->
		stack.push(nextValue)
	}

fun <V> stack(value: V, vararg values: V): Stack<V> =
	values.fold(value.onlyStack) { stack, nextValue ->
		stack.push(nextValue)
	}

fun <V> stackOrNull(vararg values: V): Stack<V>? =
	values.fold(null as Stack<V>?) { stack, value ->
		stack.push(value)
	}

fun <V, R> Stack<V>.foldTop(fn: (V) -> R): Stack.FoldedTop<V, R> =
	Stack.FoldedTop(fn(top), pop)

fun <V, R> Stack.FoldedTop<V, R>.foldPop(fn: (R, V) -> R): R =
	foldedTop.fold(pop, fn)

fun <V, R> R.fold(stackOrNull: Stack<V>?, fn: R.(V) -> R): R =
	if (stackOrNull == null) this
	else fn(this, stackOrNull.top).fold(stackOrNull.pop, fn)

val <V> Stack<V>.reverse: Stack<V>
	get() =
		foldTop { top -> top.onlyStack }.foldPop { stack, value -> stack.push(value) }

fun <V> Stack<V>.contains(value: V): Boolean =
	top == value || (pop?.contains(value) ?: false)

val <V> Stack<V>.theOnlyOrNull: The<V>?
	get() =
		if (pop != null) null else top.the

tailrec fun <V> Stack<V>.top(fn: (V) -> Boolean): V? =
	when {
		fn(top) -> top
		pop != null -> pop.top(fn)
		else -> null
	}

fun <V> Stack<V>.theOnlyOrNull(fn: (V) -> Boolean): The<V>? =
	all(fn)?.theOnlyOrNull

fun <V> Stack<V>.all(fn: (V) -> Boolean): Stack<V>? =
	nullOf<Stack<V>>()
		.fold(this) { value ->
			if (fn(value)) push(value)
			else this
		}
		?.reverse

fun <V, R> Stack<V>.map(fn: (V) -> R): Stack<R> =
	stream.map(fn).stack.reverse

fun <V, R> Stack<V>.filterMap(fn: (V) -> The<R>?): Stack<R>? =
	(null as Stack<R>?).fold(this) { value ->
		fn(value).let { mappedOrNull ->
			if (mappedOrNull == null) this
			else push(mappedOrNull.value)
		}
	}?.reverse

val Stack<*>.sizeInt: Int
	get() =
		0.fold(this) { this + 1 }

// === appendable

fun Appendable.append(stack: Stack<*>) {
	appendReversed(stack.reverse)
}

fun Appendable.appendReversed(stack: Stack<*>) {
	appendString(stack.top)
	stack.pop?.let {
		append(", ")
		appendReversed(stack.pop)
	}
}

// === reflect

fun <V, F> Stack<V>.reflect(key: Word, reflectValue: (V) -> Field<F>): Field<F> =
	key fieldTo term(
		stackWord fieldTo reverse
			.foldTop { value -> reflectValue(value).onlyStack }
			.foldPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
			.term)

fun <V> Stack<V>.reflect(reflectValue: (V) -> Field<Value>): Term<Value> =
	reverse
		.foldTop { value -> reflectValue(value).onlyStack }
		.foldPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
		.term

// === parse

fun <V> Term<Value>.parseStack(parseValue: (Field<Value>) -> V?): Stack<V>? =
	structureTermOrNull
		?.fieldStack
		?.reverse
		?.foldTop { field ->
			parseValue(field)?.onlyStack
		}
		?.foldPop { stackOrNull, field ->
			parseValue(field)?.let { value ->
				stackOrNull?.push(value)
			}
		}
