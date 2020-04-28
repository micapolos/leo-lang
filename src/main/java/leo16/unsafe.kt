package leo16

import leo.base.notNullIf
import leo13.isEmpty
import leo13.mapFirst
import leo13.onlyOrNull
import leo13.stack

val Script.thing: Script get() = thingOrNull!!
infix fun Script.get(word: String): Script = getOrNull(word)!!
val Script.last: Script get() = lastOrNull!!
val Script.previous: Script get() = previousOrNull!!
fun Script.append(sentence: Sentence) = appendOrNull(sentence)!!
val Script.listIsEmpty: Boolean get() = thing.sentenceStack.isEmpty

fun Script.match(vararg cases: Case): Script =
	sentenceStack
		.onlyOrNull!!
		.followingScript
		.sentenceStack
		.onlyOrNull!!
		.let { sentence ->
			stack(*cases).mapFirst {
				notNullIf(sentence.firstWord == selectedWord) {
					script(sentence).fn()
				}
			}
		}!!

