package leo13

import leo.base.orIfNull
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

interface Converter<A, B> : Scripting {
	fun convert(value: A): Processor<B>
}

fun <A, B> errorConverter(): Converter<A, B> =
	error.converter { tracedError() }

fun <A, B> Processor<A>.converter(): Converter<A, B> =
	converter { process(it).run { tracedError() } }

fun <A : Scripting, B> converterCapture(fn: Converter<A, B>.() -> Unit): A {
	val capturingConverter = CapturingConverter<A, B>(null)
	capturingConverter.fn()
	return capturingConverter.capturedOrNull ?: tracedError("not" lineTo script("captured"))
}

data class CapturingConverter<V : Scripting, I>(var capturedOrNull: V?) : ObjectScripting(), Converter<V, I> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "converter" lineTo script(
			"captured" lineTo script(capturedOrNull?.scriptingLine.orIfNull { "null" lineTo script() }))

	override fun convert(value: V): Processor<I> {
		if (capturedOrNull != null) return tracedError("already" lineTo script("captured"))
		capturedOrNull = value
		return errorProcessor()
	}
}
