package leo16.script

import leo13.Stack
import leo13.map
import leo13.push
import leo13.stack
import leo14.lineTo
import leo14.script
import leo14.untyped.leoString

data class Script(val sentenceStack: Stack<Sentence>) {
	override fun toString() = leo14Script.leoString
}

data class Sentence(val word: String, val script: Script) {
	override fun toString() = leo14ScriptLine.leoString
}

val Stack<Sentence>.script get() = Script(this)
operator fun Script.plus(sentence: Sentence) = sentenceStack.push(sentence).script
infix fun String.sentenceTo(script: Script) = Sentence(this, script)
fun script(vararg sentence: Sentence) = stack(*sentence).script
operator fun String.invoke(vararg sentence: Sentence) = sentenceTo(script(*sentence))

val Script.leo14Script: leo14.Script
	get() =
		sentenceStack.map { leo14ScriptLine }.script

val Sentence.leo14ScriptLine: leo14.ScriptLine
	get() =
		word.lineTo(script.leo14Script)

