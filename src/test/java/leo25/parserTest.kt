package leo25

import leo.base.assertEqualTo
import kotlin.test.Test

class ParserTest {
	@Test
	fun name() {
		nameParser.parsed("a").assertEqualTo("a")
		nameParser.parsed("foo").assertEqualTo("foo")

		nameParser.parsed("").assertEqualTo(null)
		nameParser.parsed("1").assertEqualTo(null)
		nameParser.parsed("foo1").assertEqualTo(null)
		nameParser.parsed("foo bar").assertEqualTo(null)
	}

	@Test
	fun string() {
		stringParser.parsed("\"\"").assertEqualTo("")
		stringParser.parsed("\"foo\"").assertEqualTo("foo")
		stringParser.parsed("\"foo 123\"").assertEqualTo("foo 123")
		stringParser.parsed("\"\t\"").assertEqualTo("\t")
		stringParser.parsed("\"\n\"").assertEqualTo("\n")
		stringParser.parsed("\"\\\\\"").assertEqualTo("\\")
		stringParser.parsed("\"\\\"\"").assertEqualTo("\"")

		stringParser.parsed("").assertEqualTo(null)
		stringParser.parsed("\"").assertEqualTo(null)
		stringParser.parsed("\"foo").assertEqualTo(null)
	}
}