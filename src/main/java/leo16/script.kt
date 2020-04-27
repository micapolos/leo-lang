package leo16

import leo13.*
import leo14.lineTo
import leo14.plus
import leo15.string

data class Script(val sentenceStack: Stack<Sentence>) {
	override fun toString() = leo14Script.string
}

data class Sentence(val firstWord: String, val followingScript: Script) {
	override fun toString() = leo14ScriptLine.string
}

val Stack<Sentence>.script get() = Script(this)
val emptyScript = stack<Sentence>().script
fun script(vararg sentences: Sentence) = stack(*sentences).script
operator fun Script.plus(sentence: Sentence) = sentenceStack.push(sentence).script
operator fun String.invoke(followingScript: Script) = Sentence(this, followingScript)
operator fun String.invoke(vararg sentences: Sentence) = invoke(script(*sentences))

val Script.leo14Script: leo14.Script
	get() =
		leo14.script().fold(sentenceStack.reverse) { plus(it.leo14ScriptLine) }
val Sentence.leo14ScriptLine: leo14.ScriptLine
	get() =
		firstWord lineTo followingScript.leo14Script