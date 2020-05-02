package leo16

import leo16.library.valueFunMap

val loadedValueMap = mutableMapOf<Pattern, Value>()

val Pattern.loadValue: Value
	get() =
		valueFunMap[this]!!.invoke()

val Pattern.loadedValue: Value
	get() =
		loadedValueMap.computeIfAbsent(this) { it.loadValue }
