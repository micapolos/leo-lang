package leo16

import leo13.map

fun Value.traverse(fn: Field.() -> Field): Value =
	fieldStack.map { fn().traverse(fn) }.value

fun Field.traverse(fn: Field.() -> Field): Field =
	// TODO: traverse everything
	when (this) {
		is SentenceField -> sentence.word.sentenceTo(sentence.value.traverse(fn)).field
		is FunctionField -> this
		is NativeField -> this
		is ChoiceField -> this
		is LazyField -> this
		is EvaluatedField -> this
	}
