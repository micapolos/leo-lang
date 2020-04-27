package leo16

import leo.base.notNullIf
import leo13.*
import leo15.lastName

data class Case(val selectedWord: String, val fn: Script.() -> Script)

fun String.gives(fn: Script.() -> Script) = Case(this, fn)

val Script.thing: Script
	get() =
		sentenceStack.onlyOrNull!!.followingScript

infix fun Script.get(word: String): Script =
	thing
		.sentenceStack
		.mapFirst {
			notNullIf(firstWord == word) {
				script(this)
			}
		}!!

infix fun Script.make(word: String): Script =
	script(word(this))

val Script.last: Script
	get() =
		script(lastName(thing.sentenceStack.linkOrNull!!.value))

val Script.previous: Script
	get() =
		sentenceStack.onlyOrNull!!.run {
			script(previousName(firstWord(followingScript.sentenceStack.linkOrNull!!.stack.script)))
		}

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

