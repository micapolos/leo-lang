package leo14.typed.compiler

import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.plus
import leo14.typed.typed

data class Compiled<T>(
	val memory: Memory<T>,
	val typed: Typed<T>)

fun <T> compiled(typed: Typed<T>) = Compiled(memory(), typed)

val <T> Compiled<T>.begin: Compiled<T>
	get() =
		copy(typed = typed())

fun <T> Compiled<T>.plus(line: TypedLine<T>): Compiled<T> =
	copy(typed = typed.plus(line))