package leo13

import leo13.script.ScriptLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo9.Stack
import leo9.push

data class StackScripting<V>(val stack: Stack<V>, val scriptLineFn: V.() -> ScriptLine) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "stack" lineTo stack.script { scriptLineFn() }.emptyIfEmpty
}

fun <V> Stack<V>.scripting(scriptLineFn: V.() -> ScriptLine): StackScripting<V> =
	StackScripting(this, scriptLineFn)

val <V : Scripting> Stack<V>.scripting: StackScripting<V>
	get() =
		scripting { scriptingLine }

fun <V> StackScripting<V>.update(fn: Stack<V>.() -> Stack<V>): StackScripting<V> =
	stack.fn().scripting(scriptLineFn)


fun <V> StackScripting<V>.push(value: V): StackScripting<V> =
	update { push(value) }