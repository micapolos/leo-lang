package leo.term

import leo.Word
import leo.base.string

data class Operator(
	val word: Word)

val Word.operator: Operator
	get() =
		Operator(this)

fun Appendable.append(operator: Operator): Appendable =
	append(operator.word.string)