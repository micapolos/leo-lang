package leo16

import leo13.*
import leo14.lineTo
import leo14.plus
import leo14.script
import leo14.untyped.scriptLine
import leo15.scriptName
import leo15.sentenceName
import leo15.string

data class Script(val sentenceStack: Stack<Sentence>) {
	override fun toString() = leo14Script.string
}

data class Sentence(val word: String, val script: Script) {
	override fun toString() = leo14ScriptLine.string
}

val Stack<Sentence>.script get() = Script(this)
fun script(vararg sentences: Sentence) = stack(*sentences).script
operator fun Script.plus(sentence: Sentence) = sentenceStack.push(sentence).script
operator fun String.invoke(script: Script) = Sentence(this, script)
operator fun String.invoke(vararg sentences: Sentence) = invoke(script(*sentences))

val Script.asSentence: Sentence
	get() =
		scriptName(this)

val Sentence.asSentence: Sentence
	get() =
		sentenceName(this)

val Script.leo14Script: leo14.Script
	get() =
		script().fold(sentenceStack.reverse) { plus(it.leo14ScriptLine) }

val Sentence.leo14ScriptLine: leo14.ScriptLine
	get() =
		null
			?: parseSentenceStackOrNull { leo14ScriptLine }?.let { listName lineTo script(*it.array) }
			?: literalOrNull?.scriptLine
			?: word lineTo script.leo14Script

val Script.isEmpty get() = sentenceStack.isEmpty
