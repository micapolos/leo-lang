package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold

data class PatternDictionary(val map: Map<String, Pattern>)

fun patternDictionary(empty: Empty) = PatternDictionary(emptyMap())
fun PatternDictionary.plus(line: PatternLine) = PatternDictionary(map.plus(line.name to line.pattern))
fun dictionary(line: PatternLine, vararg lines: PatternLine) =
	patternDictionary(empty).fold(line, lines) { plus(it) }

fun PatternDictionary.contains(line: Line) = map[line.name]?.contains(line.value) ?: false