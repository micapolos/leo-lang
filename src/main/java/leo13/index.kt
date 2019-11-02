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

val Index.int get() = 0.plus(this)
val Int.index get() = zero.index.plus(this)

tailrec operator fun Int.plus(index: Index): Int =
	when (index) {
		is ZeroIndex -> this
		is NextIndex -> inc().plus(index.previous)
	}

tailrec operator fun Index.plus(int: Int): Index =
	if (int == 0) this
	else next.plus(int.dec())

fun index(int: Int): Index = int.index