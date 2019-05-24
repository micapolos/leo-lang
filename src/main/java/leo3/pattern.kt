package leo3

import leo.base.Empty

sealed class Pattern

data class EmptyPattern(val empty: Empty) : Pattern()
data class NodePattern(val node: PatternNode) : Pattern()
data class PatternPattern(val pattern: Pattern) : Pattern()

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(node: PatternNode): Pattern = NodePattern(node)
fun pattern(pattern: Pattern): Pattern = PatternPattern(pattern)