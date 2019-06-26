package leo7

data class Line(val word: Word, val script: Script)

infix fun Word.lineTo(script: Script) = Line(this, script)
