package leo16

import leo.base.nullOf

data class ValueFragment(val parentOrNull: ValueParent?, val value: Value)
data class ValueParent(val fragment: ValueFragment, val word: String)

fun ValueParent?.fragment(value: Value) = ValueFragment(this, value)
fun ValueFragment.parent(word: String) = ValueParent(this, word)
val emptyFragment get() = nullOf<ValueParent>().fragment(emptyValue)

val ValueFragment.end: ValueFragment?
	get() =
		parentOrNull?.endFragment(value)

fun ValueParent.endFragment(value: Value): ValueFragment =
	fragment.plus(word(value))

fun ValueFragment.plus(sentence: Sentence): ValueFragment =
	parentOrNull.fragment(value.plus(sentence))

tailrec fun ValueFragment.rootValue(): Value {
	val ended = end
	return if (ended == null) value
	else ended.rootValue()
}