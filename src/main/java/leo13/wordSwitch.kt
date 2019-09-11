package leo13

import leo.base.fold
import leo13.generic.*
import leo13.generic.List

data class WordSwitch(val distinctCaseList: List<WordCase>)

fun uncheckedSwitch(case: WordCase, vararg cases: WordCase): WordSwitch =
	WordSwitch(list(case, *cases))

fun WordSwitch.plusResult(case: WordCase): Result<WordSwitch, CaseWordDuplicate> =
	if (containsCase(case.word)) failureResult(duplicate(case(case.word)))
	else successResult(WordSwitch(distinctCaseList.plus(case)))

fun switch(case: WordCase, vararg cases: WordCase): WordSwitch =
	WordSwitch(list(case)).fold(cases) { plusResult(it).unsafeValue }

fun WordSwitch?.orNullPlusResult(case: WordCase): Result<WordSwitch, CaseWordDuplicate> =
	this?.plusResult(case) ?: successResult(switch(case))

fun WordSwitch.containsCase(caseWord: Word): Boolean =
	distinctCaseList.any { word == caseWord }

val WordSwitch.sentenceLine: SentenceLine
	get() =
		switchWord lineTo sentence(distinctCaseList.map { sentenceLine })

fun WordSwitch.plusResult(line: SentenceLine): Result<WordSwitch, WordSwitchError> =
	if (line.word != caseWord) failureResult(wordSwitchError(mismatch(expected(caseWord), actual(line.word))))
	else wordCaseResult(line.sentence)
		.resultMapSuccess { this@plusResult.plusResult(this) } }
.resultMapFailure { wordSwitchError(this) }