package leo16

import leo13.Stack
import leo13.push
import leo13.stack

data class Script(val sentenceStack: Stack<Sentence>)
data class Sentence(val firstWord: String, val followingScript: Script)

val Stack<Sentence>.script get() = Script(this)
val emptyScript = stack<Sentence>().script
fun script(vararg sentences: Sentence) = stack(*sentences).script
operator fun Script.plus(sentence: Sentence) = sentenceStack.push(sentence).script
operator fun String.invoke(followingScript: Script) = Sentence(this, followingScript)
operator fun String.invoke(vararg sentences: Sentence) = invoke(script(*sentences))
