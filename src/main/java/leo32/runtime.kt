package leo32

import leo.binary.Bit
import leo.binary.emptyStack32
import leo.binary.int
import leo.binary.push

data class Runtime<T>(
	val state: T,
	val pushFn: T.(Bit) -> T,
	val appendCodeFn: Appendable.(T) -> Appendable)

val stackRuntime =
	Runtime(
		state = emptyStack32<Bit>(),
		pushFn = { push(it)!! },
		appendCodeFn = { appendCode(it) { appendCode(it.int) } })

fun <T> Runtime<T>.push(bit: Bit): Runtime<T> =
	copy(state = state.pushFn(bit))
