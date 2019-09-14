package leo13

import leo13.script.lineTo
import leo13.script.script
import leo9.Stack

data class ObjectConverter<S : Scripting, A, B>(
	val state: S,
	val convertFn: S.(A) -> Processor<B>
) :
	ObjectScripting(),
	Converter<A, B> {
	override fun toString() = super.toString()
	override val scriptingLine get() = "converter" lineTo script(state.scriptingLine)
	override fun convert(value: A): Processor<B> = state.convertFn(value)
}

fun <S : Scripting, A, B> S.converter(convertFn: S.(A) -> Processor<B>): Converter<A, B> =
	ObjectConverter(this, convertFn)

fun <V, T> StackScripting<V>.pushConverter(fn: StackScripting<V>.() -> Processor<T>): Converter<V, T> =
	converter {
		push(it).fn()
	}

fun <V : Scripting, T> Stack<V>.pushConverter(fn: Stack<V>.() -> Processor<T>): Converter<V, T> =
	scripting.converter { push(it).stack.fn() }
