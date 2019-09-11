package leo13

import leo.base.fold
import leo13.generic.*
import leo13.generic.List

data class LineSwitch(val distinctCaseList: List<LineCase>)

fun switch(case: LineCase, vararg cases: LineCase): LineSwitch =
	LineSwitch(list(case)).fold(cases) { plusResult(it).unsafeValue }

fun LineSwitch.plusResult(case: LineCase): Result<LineSwitch, WordDuplicate> =
	if (contains(case.word)) failureResult(duplicate(case.word))
	else successResult(LineSwitch(distinctCaseList.plus(case)))

fun LineSwitch?.orNullPlusResult(case: LineCase): Result<LineSwitch, WordDuplicate> =
	this?.plusResult(case) ?: successResult(switch(case))

fun LineSwitch.contains(word: Word): Boolean =
	distinctCaseList.any { this.word == word }

val LineSwitch.sentenceLine: SentenceLine
	get() =
		switchWord lineTo sentence(distinctCaseList.map { sentenceLine })
