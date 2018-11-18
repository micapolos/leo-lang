package leo.lab

import leo.*
import leo.base.*

val Bit.reflect: Field<Nothing>
	get() =
		bitWord fieldTo
			when (this) {
				Bit.ZERO -> zeroWord
				Bit.ONE -> oneWord
			}.term

val Byte.reflect: Field<Nothing>
	get() =
		byteWord fieldTo bitStack.reflect(Bit::reflect)

val Character.reflect: Field<Nothing>
	get() =
		characterWord fieldTo
			when (this) {
				is BeginCharacter -> beginWord.term
				is EndCharacter -> endWord.term
				is LetterCharacter -> letter.reflect.termOrNull
			}

fun <V> Stream<V>.reflect(key: Word, reflectValue: (V) -> Field<Nothing>): Field<Nothing> =
	key fieldTo term(
		stackWord fieldTo reverse
			.foldFirst { value -> reflectValue(value).term }
			.foldNext { value -> push(reflectValue(value)) })

fun <V> Stream<V>.reflect(reflectValue: V.() -> Field<Nothing>): Term<Nothing> =
	reverse
		.foldFirst { value -> reflectValue(value).term }
		.foldNext { value -> push(reflectValue(value)) }

fun <V> Stack<V>.reflect(key: Word, reflectValue: V.() -> Field<Nothing>): Field<Nothing> =
	key fieldTo term(
		stackWord fieldTo reverse
			.foldTop { value -> reflectValue(value).onlyStack }
			.foldPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
			.term)

fun <V> Stack<V>.reflect(reflectValue: (V) -> Field<Nothing>): Term<Nothing> =
	reverse
		.foldTop { value -> reflectValue(value).onlyStack }
		.foldPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
		.term

val Word.reflect: Field<Nothing>
	get() =
		wordWord fieldTo term

val Letter.reflect: Field<Nothing>
	get() =
		letterWord fieldTo char.toString().wordOrNull!!.term
