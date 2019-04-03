package leo32.base

data class Word(val string: String)
val String.word get() = Word(this)