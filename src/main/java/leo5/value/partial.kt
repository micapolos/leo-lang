package leo5.value

data class Partial(val cursor: Cursor, val word: Word)
infix fun Cursor.partialTo(word: Word) = Partial(this, word)
