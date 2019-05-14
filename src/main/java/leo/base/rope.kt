package leo.base

data class Rope<out V>(
	val previousStackOrNull: Stack<V>?,
	val current: V,
	val nextStackOrNull: Stack<V>?)

fun <V> rope(stack: Stack<V>) =
	Rope(stack.tail, stack.head, null)

fun <V> Rope<V>.moveBack() =
	previousStackOrNull?.let { previousStack ->
		Rope(previousStack.tail, previousStack.head, nextStackOrNull.push(current))
	}

fun <V> Rope<V>.moveForward() =
	nextStackOrNull?.let { nextStack ->
		Rope(previousStackOrNull.push(current), nextStack.head, nextStack.tail)
	}

tailrec fun <V> Rope<V>.moveFirst(): Rope<V> {
	val movedBack = moveBack()
	return if (movedBack == null) this
	else movedBack.moveFirst()
}

tailrec fun <V> Rope<V>.moveLast(): Rope<V> {
	val moveForward = moveForward()
	return if (moveForward == null) this
	else moveForward.moveLast()
}

fun <V, R> R.foldBackward(rope: Rope<V>?, fn: R.(V) -> R): R {
	var folded = this
	var ropeVar = rope
	while (ropeVar != null) {
		folded = folded.fn(ropeVar.current)
		ropeVar = ropeVar.moveBack()
	}
	return folded
}

fun <V, R> R.foldForward(rope: Rope<V>?, fn: R.(V) -> R): R {
	var folded = this
	var ropeVar = rope
	while (ropeVar != null) {
		folded = folded.fn(ropeVar.current)
		ropeVar = ropeVar.moveForward()
	}
	return folded
}
