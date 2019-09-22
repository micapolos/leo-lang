package leo13

import leo.base.codePointSeq
import leo.base.int
import leo.base.map
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

data class Character(val codePointInt: Int) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = characterName lineTo nameScript
}

val Character.nameScript: Script
	get() =
		when (codePointInt) {
			'('.int -> script("opening" lineTo script("bracket"))
			')'.int -> script("closing" lineTo script("bracket"))
			' '.int -> script("space")
			':'.int -> script("colon")
			'\n'.int -> script("newline")
			'\t'.int -> script("tab")
			// TODO: Escape all!!!
			else -> script(StringBuilder().appendCodePoint(codePointInt).toString())
		}

fun character(char: Char) = char.toInt().codePointCharacter
val Int.codePointCharacter get() = Character(this)
val String.characterSeq get() = codePointSeq.map { codePointCharacter }