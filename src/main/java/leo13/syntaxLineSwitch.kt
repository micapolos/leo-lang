package leo13

import leo13.generic.List
import leo13.generic.list
import leo13.generic.map

data class SyntaxSwitch(val lineList: List<SyntaxCase>)

fun switch(case: SyntaxCase, vararg cases: SyntaxCase) =
	SyntaxSwitch(list(case, *cases))

val SyntaxSwitch.sentenceLine: SentenceLine
	get() =
		switchWord lineTo sentence(lineList.map { sentenceLine })
