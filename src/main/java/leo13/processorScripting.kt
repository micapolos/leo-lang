package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo9.Stack
import leo9.push
import leo9.stack

data class ProcessorScripting<S : ScriptingObject, V>(
	val state: S,
	val processFn: S.(V) -> S) : Processor<V>, ScriptingObject() {
	override fun toString() = super.toString()
	override fun process(value: V) = state.processFn(value).processor(processFn)
	override val scriptingLine get() = "processor" lineTo script(state.scriptingLine)
}

fun <S : ScriptingObject, V> S.processor(processFn: S.(V) -> S): Processor<V> =
	ProcessorScripting(this, processFn)

fun <V> Stack<V>.processor(scriptLineFn: V.() -> ScriptLine): Processor<V> =
	scripting(scriptLineFn).let {
		it.processor { update { push(it) } }
	}

val Stack<Char>.charProcessor: Processor<Char>
	get() =
		processor { scriptLine }

fun <V> errorProcessor(): Processor<V> =
	stack<Unit>()
		.scripting { "unit" lineTo script() }
		.processor { tracedError() }
