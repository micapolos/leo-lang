package leo

import leo.base.*

data class Recurse(
	val backStack: Stack<Back>) {
	override fun toString() = reflect.string
}

val Stack<Back>.recurse: Recurse
	get() =
		Recurse(this)

fun recurse(back: Back, vararg backs: Back): Recurse =
	stack(back, *backs).recurse

val Recurse.back: Recurse?
	get() =
		backStack.pop?.recurse

fun Recurse.apply(backTraceOrNull: BackTrace?): BackTrace? =
	back.let { back ->
		if (back == null) backTraceOrNull?.back
		else back.apply(backTraceOrNull?.back)
	}

val Recurse.reflect: Term<Nothing>
	get() =
		backStack.stream.reflect(recurseWord) { backReflect }
