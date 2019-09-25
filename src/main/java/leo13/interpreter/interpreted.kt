package leo13.interpreter

import leo13.ObjectScripting
import leo13.interpretedName
import leo13.script.lineTo
import leo13.script.script

data class Interpreted(val context: InterpreterContext, val typed: ValueTyped) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			interpretedName lineTo script(context.scriptingLine, typed.scriptingLine)
}

fun interpreted(context: InterpreterContext = interpreterContext(), typed: ValueTyped = valueTyped()) =
	Interpreted(context, typed)