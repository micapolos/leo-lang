package leo16.script

import java.util.*

data class Script(val sentenceStack: Stack<Sentence>)
data class Sentence(val word: String, val script: Script)

val Stack<Sentence>.script get() = Script(this)
operator fun Script.plus(sentence: Sentence) = sentenceStack.push(sentence).script
infix fun String.sentenceTo(script: Script) = Sentence(this, script)