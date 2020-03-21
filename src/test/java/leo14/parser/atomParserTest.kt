package leo14.parser

import leo.base.assertEqualTo
import leo14.atom
import leo14.literal
import kotlin.test.Test

class AtomParserTest {
	@Test
	fun parse() {
		"".atomOrNull.assertEqualTo(null)
		"foo".atomOrNull.assertEqualTo(atom("foo"))
		"\"foo\"".atomOrNull.assertEqualTo(atom(literal("foo")))
		"123".atomOrNull.assertEqualTo(atom(literal(123)))
	}
}