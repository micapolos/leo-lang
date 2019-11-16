package leo14.parser

import leo.base.assertEqualTo
import leo14.begin
import leo14.end
import leo14.token
import kotlin.test.Test
import kotlin.test.assertFails

class TokenParserTest {
	@Test
	fun test() {
		parseToken("foo(").assertEqualTo(token(begin("foo")))
		parseToken(")").assertEqualTo(token(end))

		assertFails { parseToken("") }
		assertFails { parseToken("(") }
		assertFails { parseToken("foo((") }
		assertFails { parseToken("foo)") }
	}
}