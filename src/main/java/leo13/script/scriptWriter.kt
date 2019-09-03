package leo13.script

import leo13.base.Writer
import leo13.base.WriterObject
import leo13.fail
import leo13.token.Token

data class ScriptWriter(
	val scriptParser: ScriptParser,
	val finishFn: Script.() -> Unit) : WriterObject<Token>() {
	override fun toString() = super.toString()

	// TODO
	override val writerScriptableName get() = "parser"
	override val writerScriptableBody get() = script()

	override fun writerWrite(token: Token): Writer<Token> =
		ScriptWriter(scriptParser.push(token), finishFn)

	override val writerFinishWriting: Unit
		get() =
			scriptParser.completedScriptOrNull?.finishFn() ?: fail("finish")
}

fun scriptWriter(finishFn: Script.() -> Unit) = ScriptWriter(parser(), finishFn)