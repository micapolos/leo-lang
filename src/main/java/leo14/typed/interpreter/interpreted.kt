package leo14.typed.interpreter

import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.compiler.Memory

data class Interpreted<T>(
	val memory: Memory<T>,
	val typed: Typed<T>)

fun <T> Interpreted<T>.plus(line: TypedLine<T>): Interpreted<T> =
	TODO()