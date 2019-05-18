package leo3

import leo.base.Seq
import leo.binary.Bit

data class WordReader(
	val tokenReader: TokenReader)

val TokenReader.completedWordReader
	get() = WordReader(this)

fun WordReader.read(wordOrNull: Word?) =
	tokenReader.plus(token(wordOrNull))?.completedWordReader

val WordReader.bitSeq: Seq<Bit>
	get() = tokenReader.bitSeq