package leo14.parser

import leo.base.assertEqualTo
import leo14.number
import kotlin.test.Test
import kotlin.test.assertFails

class NumberParserTest {
	@Test
	fun test() {
		parseNumber("0").assertEqualTo(number(0))
		parseNumber("1").assertEqualTo(number(1))
		parseNumber("12").assertEqualTo(number(12))
		parseNumber("012").assertEqualTo(number(12))
		parseNumber("-0").assertEqualTo(number(-0))
		parseNumber("-01").assertEqualTo(number(-1))
		parseNumber("-012").assertEqualTo(number(-12))

		parseNumber("0.0").assertEqualTo(number(0.0))
		parseNumber("0.1").assertEqualTo(number(0.1))
		parseNumber("1.0").assertEqualTo(number(1.0))
		parseNumber("1.1").assertEqualTo(number(1.1))
		parseNumber("3.1415").assertEqualTo(number(3.1415))
		parseNumber("123.456").assertEqualTo(number(123.456))

		assertFails { parseNumber("") }
		assertFails { parseNumber(".") }
		assertFails { parseNumber(".1") }
		assertFails { parseNumber("a") }
		assertFails { parseNumber("-a") }
	}
}