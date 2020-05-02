package leo16

import leo16.library.valueFunMap

val loadedValueMap = mutableMapOf<Pattern, Value?>()

val Pattern.loadValueOrNull: Value?
	get() =
		valueFunMap[this]?.invoke()

val Pattern.loadedValueOrNull: Value?
	get() =
		loadedValueMap.computeIfAbsent(this) { it.loadValueOrNull }
