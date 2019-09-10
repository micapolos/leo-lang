package leo13

import leo.base.Empty
import leo.base.empty

data class Context(val empty: Empty)

fun context() = Context(empty)

fun Context.evaluate(sentence: Sentence): ValueOption =
	evaluator(this).plus(sentence).option