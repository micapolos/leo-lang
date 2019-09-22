package leo13.interpreter

import leo13.ObjectScripting
import leo13.compiler.Context
import leo13.compiler.context
import leo13.contextName
import leo13.expression.ValueContext
import leo13.expression.valueContext
import leo13.script.lineTo
import leo13.script.script

data class InterpreterContext(
	val compilerContext: Context,
	val valueContext: ValueContext) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			contextName lineTo script(
				compilerContext.scriptingLine,
				valueContext.scriptingLine)
}

fun interpreterContext(compilerContext: Context, valueContext: ValueContext) =
	InterpreterContext(compilerContext, valueContext)

fun interpreterContext() =
	interpreterContext(context(), valueContext())
