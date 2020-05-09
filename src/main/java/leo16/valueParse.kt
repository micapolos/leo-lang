package leo16

import leo13.Stack
import leo13.push
import leo13.reverse
import leo13.stack
import leo16.names.*

tailrec fun Stack<Value>.pushOrNull(field: Field): Stack<Value>? {
	val sentence = field.rhsOrNull(_list)?.onlyFieldOrNull?.sentenceOrNull ?: return null
	return when (sentence.word) {
		_empty ->
			if (sentence.value.isEmpty) this
			else null
		_link -> {
			val (lhs, last) = sentence.value.pairOrNull(_last) ?: return null
			val previous = lhs.rhsOrNull(_previous)?.onlyFieldOrNull ?: return null
			push(last).pushOrNull(previous)
		}
		else -> null
	}
}

val Field.stackOrNull: Stack<Value>?
	get() =
		stack<Value>().pushOrNull(this)?.reverse

val Value.stackOrNull: Stack<Value>?
	get() =
		onlyFieldOrNull?.stackOrNull

