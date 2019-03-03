package leo

import leo.base.*
import leo.binary.Bit
import leo.binary.Int5
import leo.binary.int
import leo.binary.int5

enum class Letter(val char: Char) {
	A('a'),
	B('b'),
	C('c'),
	D('d'),
	E('e'),
	F('f'),
	G('g'),
	H('h'),
	I('i'),
	J('j'),
	K('k'),
	L('l'),
	M('m'),
	N('n'),
	O('o'),
	P('p'),
	Q('q'),
	R('r'),
	S('s'),
	T('t'),
	U('u'),
	V('v'),
	W('w'),
	X('x'),
	Y('y'),
	Z('z');

	override fun toString() = reflect.string
}

val letterList =
	Letter.values().toList()

val letterCount =
	letterList.size

val letterStream: Stream<Letter> =
	Letter.values().toList().fold(nullOf<Stack<Letter>>()) { stack, letter ->
		stack.push(letter)
	}!!.reverse.stream

val charToLetterMap =
	letterList.associate { it.char to it }

val Char.letterOrNull: Letter?
	get() =
		charToLetterMap[this]

fun Appendable.append(letter: Letter): Appendable =
	append(letter.char)

val Byte.letterOrNull: Letter?
	get() =
		toChar().letterOrNull

val Letter.byte: Byte
	get() =
		char.clampedByte

val Letter.int
	get() =
		ordinal

val Letter.bitStream: Stream<Bit>
	get() =
		char.clampedByte.bitStream

val Letter.reflect: Field<Nothing>
	get() =
		letterWord fieldTo onlyWord.term

val Field<Nothing>.parseLetter: Letter?
	get() =
		matchKey(letterWord) {
			matchWord {
				letterStack.onlyOrNull
			}
		}

val Stream<Bit>.bitParseLetter: Parse<Bit, Letter>?
	get() =
		bitParseByte?.let { bitParseByte ->
			bitParseByte.parsed.letterOrNull?.let { letter ->
				bitParseByte.streamOrNull parsed letter
			}
		}

val Letter.int5
	get() =
		int.int5

val Int5.letterOrNull: Letter?
	get() =
		letterList.getOrNull(int)