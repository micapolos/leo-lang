package leo

import leo.base.*

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
		char.toByte()

val Letter.bitStream: Stream<Bit>
	get() =
		char.toByte().bitStream

val Letter.reflect: Field<Nothing>
	get() =
		letterWord fieldTo char.toString().wordOrNull!!.term

val Field<Nothing>.parseLetter: Letter?
	get() =
		match(letterWord) { letterTerm ->
			letterTerm?.onlyFieldOrNull?.word?.letterStack?.theOnlyOrNull?.value
		}
