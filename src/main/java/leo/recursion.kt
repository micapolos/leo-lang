package leo

import leo.base.Stack
import leo.base.stack
import leo.base.stream
import leo.base.string

data class Recursion(
	val backStack: Stack<Back>) {
	override fun toString() = reflect.string
}

val Stack<Back>.recursion: Recursion
	get() =
		Recursion(this)

fun recursion(back: Back, vararg backs: Back): Recursion =
	stack(back, *backs).recursion

val Recursion.back: Recursion?
	get() =
		backStack.pop?.recursion

fun Recursion.apply(backTraceOrNull: BackTrace?): BackTrace? =
	back.let { back ->
		if (back == null) backTraceOrNull?.back
		else back.apply(backTraceOrNull?.back)
	}

val Recursion.reflect: Term<Nothing>
	get() =
		backStack.stream.reflect(recurseWord) { backReflect }
