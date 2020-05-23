package leo16.script

import leo.base.charSeq
import leo.base.orNullFold
import leo.base.runIfNotNull
import leo13.isEmpty
import leo13.onlyOrNull
import leo13.push
import leo13.stack
import leo13.stackLink
import leo16.names.*

val emptyScript = stack<Block>().script
val Word.spaced get() = stackLink.spaced
val Atom.dotted get() = stackLink.dotted
fun Script.plus(block: Block) = blockStack.push(block).script
fun Dotted.plus(atom: Atom) = atomStackLink.push(atom).dotted
fun Spaced.plus(word: Word) = wordStackLink.push(word).spaced
val Script.isEmpty get() = blockStack.isEmpty
val Spaced.atomOrNull get() = wordStackLink.onlyOrNull?.atom
val Letter.word get() = stackLink.word
fun Word.plus(letter: Letter) = letterStackLink.push(letter).word
fun script(vararg blocks: Block) = stack(*blocks).script

val String.wordOrNull: Word?
	get() =
		if (isEmpty()) null
		else get(0).letterOrNull.word
			.orNullFold(substring(1).charSeq) { char ->
				runIfNotNull(char.letterOrNull) { letter ->
					plus(letter)
				}
			}

infix fun Dotted.dot(string: String): Dotted = plus(string.wordOrNull!!.atom)
infix fun String.dot(string: String): Dotted = wordOrNull!!.atom.dotted dot string

infix fun Spaced.space(string: String): Spaced = plus(string.wordOrNull!!)
infix fun String.space(string: String): Spaced = wordOrNull!!.spaced space string

infix fun Spaced.space(script: Script): Block = sectionTo(script).block

operator fun String.invoke(vararg blocks: Block): Block = wordOrNull!!.spaced.sectionTo(script(*blocks)).block

val x = _circle(
	_radius(),
	_center space _point space script(
	)
)