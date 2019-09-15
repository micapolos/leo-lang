package leo13.untyped.interpreter

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.compiler.Context
import leo13.untyped.compiler.context
import leo13.untyped.expression.ValueGiven
import leo13.untyped.expression.given
import leo13.untyped.expression.scriptLine
import leo13.untyped.value.value

data class InterpreterContext(
	val compilerContext: Context,
	val valueGiven: ValueGiven) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"context" lineTo script(
				compilerContext.scriptingLine,
				valueGiven.scriptLine)
}

fun interpreterContext(compilerContext: Context, valueGiven: ValueGiven) =
	InterpreterContext(compilerContext, valueGiven)

fun interpreterContext() =
	interpreterContext(context(), given(value()))
