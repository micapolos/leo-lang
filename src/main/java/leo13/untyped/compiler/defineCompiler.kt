package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.Processor
import leo13.script.ScriptLine
import leo13.script.todoScriptLine
import leo13.token.Token

data class DefineCompiler(
	val processor: Processor<Context>,
	val context: Context,
	val root: Boolean) : ObjectScripting(), Processor<Token> {
	override val scriptingLine: ScriptLine
		get() = todoScriptLine

	override fun process(token: Token): Processor<Token> =
		TODO()
}

fun Processor<Context>.defineCompiler(context: Context) =
	DefineCompiler(this, context, false)
