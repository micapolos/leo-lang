package leo14.parser

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class StringParserTest {
	@Test
	fun test() {
		parseString("\"\"").assertEqualTo("")
		parseString("\"foo\"").assertEqualTo("foo")

		assertFails { parseString("") }
		assertFails { parseString("\"") }
		assertFails { parseString("\"\"\"") }
	}
}