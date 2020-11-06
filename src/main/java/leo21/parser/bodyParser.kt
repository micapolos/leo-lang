package leo21.parser

import leo13.Stack
import leo13.push
import leo14.Token
import leo14.begin
import leo14.end
import leo14.token

data class BodyParser(
	val tokenStack: Stack<Token>,
	val multiIndent: MultiIndent,
	val atomParserOrNull: AtomParser?
)

fun BodyParser.plus(char: Char): BodyParser? {
	val newAtomParserOrNull =
		if (atomParserOrNull == null) char.beginAtomParser
		else atomParserOrNull.plus(char)
	return if (newAtomParserOrNull != null) copy(atomParserOrNull = newAtomParserOrNull)
	else atomParserOrNull?.end?.let { atom ->
		when (char) {
			' ' -> when (atom) {
				is LiteralAtom -> null
				is NameAtom -> copy(
					tokenStack = tokenStack.push(token(begin(atom.name))),
					multiIndent = multiIndent.plus(indent),
					atomParserOrNull = null)
			}
			'.' -> when (atom) {
				is LiteralAtom -> copy(
					tokenStack = tokenStack.push(token(atom.literal)),
					atomParserOrNull = null)
				is NameAtom -> copy(
					tokenStack = tokenStack.push(token(begin(atom.name))).push(token(end)),
					atomParserOrNull = null)
			}
			else -> null
		}
	}
}