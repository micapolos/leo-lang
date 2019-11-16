package leo14.parser

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class LiteralParserTest {
	@Test
	fun test() {
		parseLiteral("\"foo\"").assertEqualTo(literal("foo"))
		parseLiteral("123").assertEqualTo(literal(123))
	}
}