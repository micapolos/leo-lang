package leo32.treo

import leo.binary.Bit
import leo.binary.digitChar

typealias WriteFn = Bit.() -> Unit

data class Sink(
	val putFn: WriteFn)

fun Sink.put(bit: Bit) = putFn(bit)

val voidSink: Sink = Sink { Unit }

fun sinkString(fn: Sink.() -> Unit): String {
	val stringBuilder = StringBuilder()
	Sink { stringBuilder.append(digitChar) }.fn()
	return stringBuilder.toString()
}
