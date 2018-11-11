package leo.base

import leo.*

data class Stack<V>(
	val pop: Stack<V>?,
	val top: V
) {
	override fun toString() = appendableString { it.append(this) }

	data class FoldedTop<V, R>(
		val foldedTop: R,
		val pop: Stack<V>?
	)
}

fun <V> nullStack() =
	null as Stack<V>?

val <V> V.stack
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
	values.fold(value.stack) { stack, nextValue ->
		stack.push(nextValue)
	}

fun <V> stackOrNull(vararg values: V): Stack<V>? =
	values.fold(null as Stack<V>?) { stack, value ->
		stack.push(value)
	}

fun <V, R> Stack<V>.foldTop(fn: (V) -> R): Stack.FoldedTop<V, R> =
	Stack.FoldedTop(fn(top), pop)

fun <V, R> Stack.FoldedTop<V, R>.andPop(fn: (R, V) -> R): R =
	pop.fold(foldedTop, fn)

fun <V, R> Stack<V>?.fold(initial: R, fn: (R, V) -> R): R =
	if (this == null) initial
	else pop.fold(fn(initial, top), fn)

fun <V, R> R.fold2(stackOrNull: Stack<V>?, fn: R.(V) -> R): R =
	stackOrNull.fold(this) { folded, value ->
		folded.fn(value)
	}

val <V> Stack<V>.reverse: Stack<V>
	get() =
		foldTop { top -> top.stack }.andPop { stack, value -> stack.push(value) }

fun <V> Stack<V>.contains(value: V): Boolean =
	top == value || (pop?.contains(value) ?: false)

val <V> Stack<V>.only: V?
	get() =
		if (pop != null) null else top

tailrec fun <V> Stack<V>.top(fn: (V) -> Boolean): V? =
	when {
		fn(top) -> top
		pop != null -> pop.top(fn)
		else -> null
	}

fun <V> Stack<V>.only(fn: (V) -> Boolean): V? =
	all(fn)?.only

fun <V> Stack<V>.all(fn: (V) -> Boolean): Stack<V>? =
	fold(null as Stack<V>?) { all, value ->
		if (fn(value)) all.push(value)
		else all
	}?.reverse

fun <V, R> Stack<V>.map(fn: (V) -> R): Stack<R> =
	reverse
		.foldTop { top -> fn(top).stack }
		.andPop { stack, value -> stack.push(fn(value)) }

fun <V, R> Stack<V>.filterMap(fn: (V) -> R?): Stack<R>? =
	fold(null as Stack<R>?) { all, value ->
		fn(value).let { mappedOrNull ->
			if (mappedOrNull == null) all
			else all.push(mappedOrNull)
		}
	}?.reverse

val Stack<*>.sizeInt: Int
	get() =
		fold(0) { size, _ -> size + 1 }

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
			.foldTop { value -> reflectValue(value).stack }
			.andPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
			.term)

fun <V> Stack<V>.reflect(reflectValue: (V) -> Field<Nothing>): Term<Nothing> =
	reverse
		.foldTop { value -> reflectValue(value).stack }
		.andPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
		.term

// === parse

fun <V> Term<Nothing>.parseStack(parseValue: (Field<Nothing>) -> V?): Stack<V>? =
	structureTermOrNull
		?.fieldStack
		?.reverse
		?.foldTop { field ->
			parseValue(field)?.stack
		}
		?.andPop { stackOrNull, field ->
			parseValue(field)?.let { value ->
				stackOrNull?.push(value)
			}
		}
