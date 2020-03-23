package leo14.untyped

import leo.bellChar

val thunkBellOnEval = false

sealed class Thunk

data class ProgramThunk(val program: Program) : Thunk() {
	override fun toString() = program.toString()
}

data class LazyThunk(val lazy: Lazy) : Thunk() {
	override fun toString() = lazy.toString()
}

fun thunk(program: Program): Thunk = ProgramThunk(program)
fun thunk(lazy: Lazy): Thunk = LazyThunk(lazy)

val Thunk.program
	get() =
		when (this) {
			is ProgramThunk -> program
			is LazyThunk -> lazy.program.also { if (thunkBellOnEval) print(bellChar) }
		}

fun Thunk.plus(value: Value): Thunk =
	thunk(program(this sequenceTo value))