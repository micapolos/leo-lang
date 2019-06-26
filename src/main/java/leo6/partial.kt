package leo6

data class Partial(val cursor: Cursor, val word: Word)
infix fun Cursor.partialTo(word: Word) = Partial(this, word)
