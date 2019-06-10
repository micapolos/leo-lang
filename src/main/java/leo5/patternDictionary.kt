package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold

data class PatternDictionary(val map: Map<String, Pattern>)

fun patternDictionary(empty: Empty) = PatternDictionary(emptyMap())
fun PatternDictionary.apply(line: PatternLine) = PatternDictionary(map.plus(line.name to line.pattern))
fun dictionary(line: PatternLine, vararg lines: PatternLine) =
	patternDictionary(empty).fold(line, lines) { apply(it) }

fun PatternDictionary.contains(line: ValueLine) = map[line.name]?.contains(line.value) ?: false