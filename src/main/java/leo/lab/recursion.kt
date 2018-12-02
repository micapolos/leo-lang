package leo.lab

import leo.base.Back
import leo.base.Stack
import leo.base.stack

data class Recursion(
	val backStack: Stack<Back>) {
	//override fun toString() = reflect.string
}

val Stack<Back>.recursion: Recursion
	get() =
		Recursion(this)

fun recursion(back: Back, vararg backs: Back): Recursion =
	stack(back, *backs).recursion

val Recursion.back: Recursion?
	get() =
		backStack.tail?.recursion

fun Recursion.apply(backTraceOrNull: BackTrace?): BackTrace? =
	if (backTraceOrNull == null) null
	else back.let { back ->
		if (back == null) backTraceOrNull
		else back.apply(backTraceOrNull.back)
	}

//val Recursion.reflect: Term<Nothing>
//	get() =
//		backStack.stream.reflect(recurseWord) { backReflect }
