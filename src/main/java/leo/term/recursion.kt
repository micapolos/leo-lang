package leo.term

import leo.base.Stack
import leo.base.stack

data class Recursion(
	val backStack: Stack<Back>) {
	override fun toString() = "recursion($backStack)"
}

val Stack<Back>.recursion: Recursion
	get() =
		Recursion(this)

fun recursion(back: Back, vararg backs: Back): Recursion =
	stack(back, *backs).recursion