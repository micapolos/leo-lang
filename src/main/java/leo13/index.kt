package leo13

import leo.binary.Zero
import leo.binary.zero

sealed class Index {
	override fun toString() = "index($int)"
}

data class ZeroIndex(val zero: Zero) : Index() {
	override fun toString() = super.toString()
}

data class NextIndex(val previous: Index) : Index() {
	override fun toString() = super.toString()
}

val Zero.index: Index get() = ZeroIndex(this)
val Index.next: Index get() = NextIndex(this)
val Index.previousOrNull: Index?
	get() =
		when (this) {
			is ZeroIndex -> null
			is NextIndex -> previous
		}

val Index.previousOrZero: Index
	get() =
		previousOrNull?:index0

val Index.int get() = 0.plus(this)
val Int.index get() = zero.index.plus(this)

val index0 = zero.index
val index1 = index0.next
val index2 = index1.next
val index3 = index2.next

tailrec operator fun Int.plus(index: Index): Int =
	when (index) {
		is ZeroIndex -> this
		is NextIndex -> inc().plus(index.previous)
	}

tailrec operator fun Index.plus(int: Int): Index =
	if (int == 0) this
	else next.plus(int.dec())

fun index(int: Int): Index = int.index

// TODO: Make it tail-recursive
fun Index.max(index: Index): Index =
	when (this) {
		is ZeroIndex -> index
		is NextIndex ->
			when (index) {
				is ZeroIndex -> this
				is NextIndex -> previous.max(index.previous).next
			}
	}