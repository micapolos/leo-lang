package leo13.untyped.interpreter

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.compiler.Context
import leo13.untyped.compiler.context
import leo13.untyped.expression.ValueContext
import leo13.untyped.expression.valueContext

data class InterpreterContext(
	val compilerContext: Context,
	val valueContext: ValueContext) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"context" lineTo script(
				compilerContext.scriptingLine,
				valueContext.scriptingLine)
}

fun interpreterContext(compilerContext: Context, valueContext: ValueContext) =
	InterpreterContext(compilerContext, valueContext)

fun interpreterContext() =
	interpreterContext(context(), valueContext())
