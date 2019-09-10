package leo13

import leo13.generic.List
import leo13.generic.list
import leo13.generic.map

data class WordSwitch(val lineList: List<WordCase>)

fun switch(case: WordCase, vararg cases: WordCase) =
	WordSwitch(list(case, *cases))

val WordSwitch.sentenceLine: SentenceLine
	get() =
		switchWord lineTo sentence(lineList.map { sentenceLine })
