package leo16

import leo.base.notNullIf
import leo13.*
import leo15.lastName
import leo15.linkName
import leo15.nodeName
import leo15.previousName

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
	thingOrNull
		?.sentenceStack
		?.mapFirst {
			notNullIf(firstWord == word) {
				script(this)
			}
		}

infix fun Script.make(word: String): Script =
	script(word(this))

val Script.lastOrNull: Script?
	get() =
		matchPrefix(listName) { rhs ->
			rhs.sentenceStack.linkOrNull?.let { link ->
				script(lastName(link.value))
			}
		}

val Script.previousOrNull: Script?
	get() =
		matchPrefix(listName) { rhs ->
			rhs.sentenceStack.linkOrNull?.let { link ->
				script(previousName(listName(link.stack.script)))
			}
		}

fun Script.appendOrNull(sentence: Sentence): Script? =
	matchPrefix(listName) { rhs ->
		script(listName(rhs.plus(sentence)))
	}

val Script.nodeOrNull: Script?
	get() =
		matchPrefix(listName) { rhs ->
			script(
				nodeName(
					rhs.sentenceStack.linkOrNull
						?.run {
							script(
								linkName(
									previousName(stack.script.make(listName)),
									lastName(value)))
						}
						?: script(emptyName())))
		}
