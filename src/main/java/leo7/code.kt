package leo7

import leo.base.fold
import leo.base.runIf

fun Appendable.appendCode(letter: Letter) =
	append(letter.char)

fun Appendable.appendCode(extension: WordExtension) =
	appendCode(extension.word).appendCode(extension.letter)

fun Appendable.appendCode(word: Word): Appendable =
	when (word) {
		is LetterWord -> appendCode(word.letter)
		is ExtensionWord -> appendCode(word.extension)
	}

fun Appendable.appendCode(line: Line, parenthesis: Boolean = false) =
	this
		.runIf(parenthesis) { append('(') }
		.appendCode(line.word)
		.appendLineCode(line.script)
		.runIf(parenthesis) { append(')') }

fun Appendable.appendLineCode(script: Script) =
	runIf(script.lineStackOrNull != null) { append(": ").appendCode(script) }

fun Appendable.appendCode(script: Script): Appendable =
	(script.lineStackOrNull?.tail == null).let { singleLine ->
		fold(script.lineStackOrNull) { appendCode(it, !singleLine) }
	}