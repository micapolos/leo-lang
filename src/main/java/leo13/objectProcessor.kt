package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo9.Stack
import leo9.push
import leo9.stack

data class ObjectProcessor<S : Scripting, V>(
	val state: S,
	val processFn: S.(V) -> S) : Processor<V>, ObjectScripting() {
	override fun toString() = super.toString()
	override fun process(value: V) = state.processFn(value).processor(processFn)
	override val scriptingLine get() = "processor" lineTo script(state.scriptingLine)
}

fun <S : Scripting, V> S.processor(processFn: S.(V) -> S): Processor<V> =
	ObjectProcessor(this, processFn)

fun <V> Stack<V>.pushProcessor(scriptLineFn: V.() -> ScriptLine): Processor<V> =
	scripting(scriptLineFn).let {
		it.processor { update { push(it) } }
	}

fun <V : Scripting> processor(vararg values: V): Processor<V> =
	stack(*values).pushProcessor { scriptingLine }

val Stack<Char>.charPushProcessor: Processor<Char>
	get() =
		pushProcessor { scriptLine }

val charProcessor = stack<Char>().charPushProcessor

fun <V> errorProcessor(): Processor<V> =
	error.processor { tracedError() }

fun <V> voidProcessor(): Processor<V> =
	unitScripting.processor { unitScripting }