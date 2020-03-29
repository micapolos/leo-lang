package leo14.parser

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class StringParserTest {
	@Test
	fun test() {
		parseString("\"\"").assertEqualTo("")
		parseString("\"foo\"").assertEqualTo("foo")
		parseString("\"tnt\"").assertEqualTo("tnt")
		parseString("\"\\\"foo\"").assertEqualTo("\"foo")
		parseString("\"foo\\nbar\"").assertEqualTo("foo\nbar")
		parseString("\"foo\\tbar\"").assertEqualTo("foo\tbar")
		parseString("\"foo\\\\bar\"").assertEqualTo("foo\\bar")

		assertFails { parseString("") }
		assertFails { parseString("\"") }
		assertFails { parseString("\"\"\"") }
	}
}