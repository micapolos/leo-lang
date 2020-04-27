package leo16

import leo13.Stack
import leo13.linkOrNull
import leo13.push

data class Writer(val openingStack: Stack<Opening>, val script: Script)
data class Opening(val script: Script, val word: String)

fun Writer.begin(word: String): Writer =
	Writer(openingStack.push(Opening(script, word)), script())

val Writer.end: Writer?
	get() =
		openingStack.linkOrNull?.let { openingLink ->
			Writer(openingLink.stack, openingLink.value.script.plus(openingLink.value.word(script)))
		}
