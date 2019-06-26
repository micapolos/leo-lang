package leo7

data class WordBegin(val word: Word)

val Word.begin get() = WordBegin(this)
