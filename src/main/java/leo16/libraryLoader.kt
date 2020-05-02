package leo16

import leo16.library.valueFunMap

val loadedValueMap = mutableMapOf<Sentence, Value>()

val Sentence.loadValue: Value
	get() =
		valueFunMap[this]!!.invoke()

val Sentence.loadedValue: Value
	get() =
		loadedValueMap.computeIfAbsent(this) { it.loadValue }
