package leo6

import leo.base.notNullIf

data class Line(val word: Word, val script: Script) {
	override fun toString() = "$word($script)"
}

infix fun Word.lineTo(script: Script) = Line(this, script)
infix fun String.lineTo(script: Script) = word(this) lineTo script
infix fun String.lineTo(string: String) = lineTo(script(string))
fun Line.at(word: Word) = notNullIf(this.word == word) { script }

val Line.pathOrNull get() = script.pathOrNull?.let { word pathTo it }

fun Line.contains(link: PathLink) =
	word == link.firstWord && script.contains(link.remainingPath)