package leo16.script

import leo.base.AppendableIndented
import leo.base.append
import leo.base.indented
import leo.base.runIf
import leo13.asStack
import leo13.fold
import leo13.foldRight
import leo13.reverse

fun AppendableIndented.append(letter: Letter): AppendableIndented =
	append(letter.char)

fun AppendableIndented.append(word: Word): AppendableIndented =
	foldRight(word.letterStackLink.asStack) { append(it) }

fun AppendableIndented.append(atom: Atom): AppendableIndented =
	when (atom) {
		is StringAtom -> append(atom.string)
		is WordAtom -> append(atom.word)
	}

fun AppendableIndented.append(spaced: Spaced): AppendableIndented =
	this
		.foldRight(spaced.wordStackLink.stack.reverse) { append(it).append(' ') }
		.append(spaced.wordStackLink.value)

fun AppendableIndented.append(dotted: Dotted): AppendableIndented =
	this
		.foldRight(dotted.atomStackLink.stack.reverse) { append(it).append('.') }
		.append(dotted.atomStackLink.value)

fun AppendableIndented.append(section: Section): AppendableIndented =
	append(section.spaced).indented { append(section.script) }

fun AppendableIndented.append(block: Block): AppendableIndented =
	when (block) {
		is DottedBlock -> append(block.dotted)
		is SectionBlock -> append(block.section)
	}

fun AppendableIndented.append(script: Script): AppendableIndented =
	(false to this).fold(script.blockStack.reverse) {
		let { (newline, appendable) ->
			true to appendable
				.runIf(newline) { append('\n') }
				.append(it)
		}
	}.second
