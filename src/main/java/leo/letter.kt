package leo

import leo.base.string

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

  override fun toString() = reflect<Nothing>().string
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

fun <V> Letter.reflect(): Field<V> =
    letterWord fieldTo term(char.toString().wordOrNull!!)