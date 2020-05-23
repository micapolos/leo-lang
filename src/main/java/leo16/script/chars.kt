package leo16.script

val letterList = Letter.values().toList()
val firstLetterChar: Char = letterList[0].char
val Letter.char: Char get() = (ordinal + firstLetterChar.toInt()).toChar()
val Char.letterOrNull get() = letterList[this - firstLetterChar]