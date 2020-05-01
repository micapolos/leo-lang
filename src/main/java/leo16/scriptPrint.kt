package leo16

import leo13.map
import leo13.onlyOrNull
import leo15.listName

val Script.printScript: Script
	get() =
		null
			?: listPrintScriptOrNull
			?: structPrintScript

val Script.listPrintScriptOrNull: Script?
	get() =
		sentenceStack.onlyOrNull
			?.parseSentenceStackOrNull { printSentence }
			?.let { script(listName(it.script)) }

val Script.structPrintScript: Script
	get() =
		sentenceStack.map { printSentence }.script

val Sentence.printSentence: Sentence
	get() =
		word.invoke(script.printScript)
