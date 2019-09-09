package leo13

import leo.base.Empty
import leo.base.empty

data class Context(val empty: Empty)

fun context() = Context(empty)

fun Context.evaluate(sentence: Sentence): ValueScript =
	evaluator(this).plus(sentence).script