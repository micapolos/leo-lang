package leo14.typed.compiler

import leo14.lambda.NativeApply
import leo14.lambda.arg0
import leo14.lambda.eval
import leo14.typed.*

data class Compiled<T>(
	val memory: Memory<T>,
	val typed: Typed<T>)

fun <T> compiled(typed: Typed<T>, memory: Memory<T> = memory()) = Compiled(memory, typed)

val <T> Compiled<T>.begin: Compiled<T>
	get() =
		copy(typed = typed())

fun <T> Compiled<T>.beginDoes(type: Type): Compiled<T> =
	copy(typed = arg0<T>() of type)

fun <T> Compiled<T>.updateTyped(fn: Typed<T>.() -> Typed<T>): Compiled<T> =
	copy(typed = typed.fn())

fun <T> Compiled<T>.updateMemory(fn: Memory<T>.() -> Memory<T>): Compiled<T> =
	copy(memory = memory.fn())

fun <T> Compiled<T>.resolve(line: TypedLine<T>, context: Context<T>): Compiled<T> =
	updateTyped {
		plus(line)
			.run {
				memory.resolve(this) ?: context.resolve(this) ?: resolve ?: this
			}
	}

fun <T> Compiled<T>.resolve(phase: Phase, nativeInvoke: NativeApply<T>): Compiled<T> =
	when (phase) {
		Phase.COMPILER -> this
		Phase.EVALUATOR -> eval(nativeInvoke)
	}

val <T> Compiled<T>.resolveForEnd: Compiled<T>
	get() =
		compiled(memory.ret(typed))

fun <T> Compiled<T>.eval(nativeInvoke: NativeApply<T>) =
	updateTyped { resolveForEnd.typed.term.eval(nativeInvoke) of typed.type }

val <T> Compiled<T>.use
	get() =
		this
			.updateMemory { plus(remember(type("used") does resolveForEnd.typed)) }
			.updateTyped { typed() }
