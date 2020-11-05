package leo21.parser

import leo.base.orNullIf
import leo13.charString
import leo14.number

val letter = char.failIf { !it.isLetter() }
val digit = char.failIf { !it.isDigit() }

val name = letter.stackParser.map { it.charString.orNullIf { isEmpty() } }
val number = digit.stackParser.map { it.charString.toBigDecimalOrNull()?.let { number(it) } }
val string = parser('\"').to(stringParser).map { it.second }