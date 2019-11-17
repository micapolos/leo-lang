package leo14.typed.compiler

import leo14.lambda.arg0
import leo14.typed.*

data class Compiled<T>(
	val memory: Memory<T>,
	val typed: Typed<T>)

fun <T> compiled(typed: Typed<T>) = Compiled(memory(), typed)

val <T> Compiled<T>.begin: Compiled<T>
	get() =
		copy(typed = typed())

fun <T> Compiled<T>.beginDoes(type: Type): Compiled<T> =
	copy(typed = arg0<T>() of type)

fun <T> Compiled<T>.updateTyped(fn: Typed<T>.() -> Typed<T>): Compiled<T> =
	copy(typed = typed.fn())

fun <T> Compiled<T>.resolve(line: TypedLine<T>): Compiled<T> =
	updateTyped {
		plus(line)
			.run {
				// TODO: Resolve memory.
				resolve ?: this
			}
	}

fun <T> Compiled<T>.ret(typed: Typed<T>): Typed<T> =
	memory.ret(typed)
