package leo7

import leo.base.orNull

sealed class Word
data class LetterWord(val letter: Letter): Word()
data class ExtensionWord(val extension: WordExtension): Word()

val Letter.word: Word get() = LetterWord(this)
operator fun Word.plus(letter: Letter): Word = ExtensionWord(extensionWith(letter))

val String.wordOrNull
	get() =
		fold(newWordParser.orNull) { wordReader, char ->
			wordReader?.read(char)
		}?.parsedWordOrNull
