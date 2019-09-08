package leo13

data class Value(val sentence: Sentence, val pattern: Pattern)

infix fun Sentence.valueOf(pattern: Pattern) = Value(this, pattern)
