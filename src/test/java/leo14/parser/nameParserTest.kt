package leo14.parser

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class NameParserTest {
	@Test
	fun test() {
		parseName("foo").assertEqualTo("foo")
		parseName("ząb").assertEqualTo("ząb")

		assertFails { parseName("") }
		assertFails { parseName("0") }
		assertFails { parseName("a4") }
	}
}