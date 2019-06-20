@file:JvmName("ScriptLinkKt")

package leo5.value

data class ParentLink(val cursor: Cursor, val word: Word)
infix fun Cursor.linkTo(word: Word) = ParentLink(this, word)
