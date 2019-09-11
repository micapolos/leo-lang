package leo13

import leo.base.fold
import leo13.generic.*
import leo13.generic.List

data class WordSwitch(val distinctCaseList: List<WordCase>)

fun switch(distinctCaseList: List<WordCase>) = WordSwitch(distinctCaseList)

fun WordSwitch.plusResult(case: WordCase): Result<WordSwitch, WordDuplicate> =
	if (containsCase(case.word)) failureResult(duplicate(case.word))
	else successResult(WordSwitch(distinctCaseList.plus(case)))

fun switch(case: WordCase, vararg cases: WordCase): WordSwitch =
	WordSwitch(list(case)).fold(cases) { plusResult(it).unsafeValue }

fun WordSwitch?.orNullPlusResult(case: WordCase): Result<WordSwitch, WordDuplicate> =
	this?.plusResult(case) ?: successResult(switch(case))

fun WordSwitch.containsCase(caseWord: Word): Boolean =
	distinctCaseList.any { word == caseWord }

val WordSwitch.sentenceLine: SentenceLine
	get() =
		switchWord lineTo sentence(distinctCaseList.map { sentenceLine })
