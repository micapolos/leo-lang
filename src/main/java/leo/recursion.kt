package leo

import leo.base.Stack
import leo.base.stack
import leo.base.stream
import leo.base.string

data class Recursion(
	val backStack: Stack<GoBack>) {
	override fun toString() = reflect.string
}

val Stack<GoBack>.recursion: Recursion
	get() =
		Recursion(this)

fun recursion(goBack: GoBack, vararg goBacks: GoBack): Recursion =
	stack(goBack, *goBacks).recursion

val Recursion.goBack: Recursion?
	get() =
		backStack.pop?.recursion

fun Recursion.apply(backTraceOrNull: BackTrace?): BackTrace? =
	goBack.let { back ->
		if (back == null) backTraceOrNull?.back
		else back.apply(backTraceOrNull?.back)
	}

val Recursion.reflect: Field<Nothing>
	get() =
		recursionWord fieldTo backStack.stream.reflect { goBackReflect }
