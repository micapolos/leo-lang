package leo7

import leo.base.Stack
import leo.base.orNull
import leo.base.push
import leo.base.stack

data class Word(val letterStack: Stack<Letter>)

val Letter.word get() = Word(stack(this))
operator fun Word.plus(letter: Letter) = Word(letterStack.push(letter))

val String.wordOrNull
	get() =
		fold(newWordParser.orNull) { wordReader, char ->
			wordReader?.read(char)
		}?.parsedWordOrNull
