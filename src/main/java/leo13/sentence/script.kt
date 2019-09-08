package leo13.sentence

import leo.base.appendableString
import leo.base.fold
import leo.base.updateIfNotNull

data class Script(val sentenceOrNull: Sentence?) {
	override fun toString() = appendableString { it.append(this) }
}

fun script(sentenceOrNull: Sentence?) = Script(sentenceOrNull)
infix fun Script.plus(line: Line): Script = script(sentenceOrNull?.run { plus(line) } ?: sentence(line))
infix fun Script.plus(word: String): Script = script(sentenceOrNull?.run { plus(word) } ?: sentence(word))
fun script(vararg lines: Line): Script = script(null).fold(lines) { plus(it) }
fun Appendable.append(script: Script) = updateIfNotNull(script.sentenceOrNull) { append(it) }