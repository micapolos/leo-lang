package leo13

import leo13.script.ScriptLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo9.Stack

data class StackScripting<V>(val stack: Stack<V>, val scriptLineFn: V.() -> ScriptLine) : ScriptingObject() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "stack" lineTo stack.script { scriptLineFn() }.emptyIfEmpty
}

fun <V> Stack<V>.scripting(scriptLineFn: V.() -> ScriptLine): StackScripting<V> =
	StackScripting(this, scriptLineFn)

fun <V> StackScripting<V>.update(fn: Stack<V>.() -> Stack<V>): StackScripting<V> =
	stack.fn().scripting(scriptLineFn)

