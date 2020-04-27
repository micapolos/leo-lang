package leo16

import leo.base.notNullIf
import leo13.linkOrNull
import leo13.mapFirst
import leo13.onlyOrNull
import leo13.stack

data class Case(val selectedWord: String, val fn: Script.() -> Script)

fun String.gives(fn: Script.() -> Script) = Case(this, fn)

infix fun Script.get(word: String): Script =
	sentenceStack
		.onlyOrNull!!
		.followingScript
		.sentenceStack
		.mapFirst {
			notNullIf(firstWord == word) {
				script(this)
			}
		}!!

infix fun Script.make(word: String): Script =
	script(word(this))

val Script.thing: Script
	get() =
		sentenceStack.onlyOrNull!!.followingScript

val Script.tail: Script
	get() =
		sentenceStack.linkOrNull!!.stack.script

val Script.head: Script
	get() =
		script(sentenceStack.linkOrNull!!.value)

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

