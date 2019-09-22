package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus

data class TextLink(val text: Text, val character: Character) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = linkName lineTo text.scriptingLine.rhs.plus(character.scriptingLine)
}

infix fun Text.linkTo(character: Character) = TextLink(this, character)
