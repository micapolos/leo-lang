package leo13.compiler

import leo13.ObjectScripting
import leo13.compiledName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class Compiled(val context: Context, val typed: TypedExpression): ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = compiledName lineTo script(context.scriptingLine, typed.scriptingLine)
}

fun compiled(context: Context, typed: TypedExpression) = Compiled(context, typed)
