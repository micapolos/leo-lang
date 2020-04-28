package leo16

import leo.base.notNullIf
import leo13.*
import leo15.lastName

val Script.isEmpty: Boolean
	get() =
		sentenceStack.isEmpty

val Script.normalize: Script
	get() =
		sentenceStack.linkOrNull?.let { sentenceLink ->
			notNullIf(sentenceLink.value.followingScript.isEmpty) {
				script(sentenceLink.value.firstWord(sentenceLink.stack.script))
			}
		} ?: this

val Script.thingOrNull: Script?
	get() =
		sentenceStack.onlyOrNull?.followingScript

infix fun Script.getOrNull(word: String): Script? =
	thing
		.sentenceStack
		.mapFirst {
			notNullIf(firstWord == word) {
				script(this)
			}
		}

infix fun Script.make(word: String): Script =
	script(word(this))

val Script.lastOrNull: Script?
	get() =
		thing.sentenceStack.linkOrNull?.let { link ->
			script(lastName(link.value))
		}

val Script.previouOrNull: Script?
	get() =
		sentenceStack.onlyOrNull?.run {
			followingScript.sentenceStack.linkOrNull?.let { link ->
				script(previousName(firstWord(link.stack.script)))
			}
		}

fun Script.appendOrNull(sentence: Sentence): Script? =
	sentenceStack.onlyOrNull?.run {
		script(firstWord(followingScript.plus(sentence)))
	}

