package leo13.pattern

sealed class Type

data class PatternType(val pattern: Pattern) : Type()
data class RecurseType(val traced: Recurse) : Type()

fun type(pattern: Pattern): Type = PatternType(pattern)
fun type(recurse: Recurse): Type = RecurseType(recurse)
