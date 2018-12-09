package leo.term

import leo.Word

data class Key(
	val word: Word)

val Word.key: Key
	get() =
		Key(this)