package leo16

data class Libraried(val library: Library, val pattern: Pattern)

fun Library.libraried(pattern: Pattern) = Libraried(this, pattern)