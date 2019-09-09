package leo13

data class SentenceScriptLine(val word: Word, val script: SentenceScript)

infix fun Word.lineTo(script: SentenceScript) = SentenceScriptLine(this, script)
