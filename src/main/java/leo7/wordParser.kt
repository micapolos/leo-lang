package leo7

import leo.base.nullOf

data class WordParser(val parsedWordOrNull: Word?)

val Word?.parser get() = WordParser(this)
val newWordParser get() = nullOf<Word>().parser

fun WordParser.read(char: Char) =
	char.letterOrNull?.let { letter ->
		(parsedWordOrNull?.plus(letter) ?: letter.word).parser
	}
