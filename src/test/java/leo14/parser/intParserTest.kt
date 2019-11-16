package leo14.parser

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class IntParserTest {
	@Test
	fun test() {
		parseInt("0").assertEqualTo(0)
		parseInt("1").assertEqualTo(1)
		parseInt("12").assertEqualTo(12)
		parseInt("012").assertEqualTo(12)
		parseInt("-0").assertEqualTo(0)
		parseInt("-01").assertEqualTo(-1)
		parseInt("-012").assertEqualTo(-12)
		parseInt(Int.MAX_VALUE.toString()).assertEqualTo(Int.MAX_VALUE)
		parseInt(Int.MIN_VALUE.toString()).assertEqualTo(Int.MIN_VALUE)

		assertFails { parseInt("") }
		assertFails { parseInt("a") }
		assertFails { parseInt("-a") }
		assertFails { parseInt(Int.MAX_VALUE.toLong().inc().toString()) }
		assertFails { parseInt(Int.MIN_VALUE.toLong().dec().toString()) }
	}
}