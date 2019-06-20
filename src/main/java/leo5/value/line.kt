package leo5.value

import leo.base.notNullIf

data class Line(val word: Word, val script: Script)
infix fun Word.lineTo(script: Script) = Line(this, script)
fun Line.at(word: Word) = notNullIf(this.word == word) { script }
