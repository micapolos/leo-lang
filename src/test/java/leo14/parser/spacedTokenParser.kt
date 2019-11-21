package leo14.parser

import leo.base.assertEqualTo
import leo14.begin
import leo14.end
import leo14.literal
import leo14.token
import kotlin.test.Test
import kotlin.test.assertFails

class SpacedTokenParserTest {
	@Test
	fun test() {
		"123 ".spacedToken.assertEqualTo(token(literal(123)))
		"-1 ".spacedToken.assertEqualTo(token(literal(-1)))
		"\"foo\" ".spacedToken.assertEqualTo(token(literal("foo")))
		"foo ".spacedToken.assertEqualTo(token(begin("foo")))
		" ".spacedToken.assertEqualTo(token(end))

		assertFails { "".spacedToken }
		assertFails { "  ".spacedToken }
		assertFails { "foo".spacedToken }
		assertFails { "foo  ".spacedToken }
		assertFails { "123".spacedToken }
		assertFails { "123  ".spacedToken }
	}
}