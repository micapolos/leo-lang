package leo14.parser

import leo.base.assertEqualTo
import leo14.begin
import leo14.end
import leo14.literal
import leo14.token
import kotlin.test.Test
import kotlin.test.assertFails

class TokenParserTest {
	@Test
	fun test() {
		parseToken("foo(").assertEqualTo(token(begin("foo")))
		parseToken(")").assertEqualTo(token(end))
		parseToken("15").assertEqualTo(token(literal(15)))
		parseToken("-123").assertEqualTo(token(literal(-123)))
		parseToken("\"foo\"").assertEqualTo(token(literal("foo")))

		assertFails { parseToken("") }
		assertFails { parseToken("(") }
		assertFails { parseToken("foo((") }
		assertFails { parseToken("foo)") }
	}
}