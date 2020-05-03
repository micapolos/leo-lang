package leo16

data class Dictionarian(val dictionary: Dictionary, val pattern: Pattern)

fun Dictionary.dictionarian(pattern: Pattern) = Dictionarian(this, pattern)