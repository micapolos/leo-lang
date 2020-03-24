package leo14.untyped

import leo.bellChar

val thunkBellOnEval = false

sealed class Thunk

data class ProgramThunk(val value: Value) : Thunk() {
	override fun toString() = value.toString()
}

data class LazyThunk(val lazy: Lazy) : Thunk() {
	override fun toString() = lazy.toString()
}

fun thunk(value: Value): Thunk = ProgramThunk(value)
fun thunk(lazy: Lazy): Thunk = LazyThunk(lazy)

val Thunk.value
	get() =
		when (this) {
			is ProgramThunk -> value
			is LazyThunk -> lazy.value.also { if (thunkBellOnEval) print(bellChar) }
		}

fun Thunk.plus(line: Line): Thunk =
	thunk(value(this sequenceTo line))