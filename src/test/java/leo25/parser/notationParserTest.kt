package leo25.parser

import leo.base.assertEqualTo
import leo14.literal
import leo25.atom
import leo25.chain
import org.junit.Test

class NotationParserTest {
	@Test
	fun atomParser() {
		atomParser.run {
			parsed("\"foo\"").assertEqualTo(atom(literal("foo")))
			parsed("123").assertEqualTo(atom(literal(123)))
			parsed("foo").assertEqualTo(atom("foo"))
			parsed(".").assertEqualTo(null)
		}
	}

	@Test
	fun chainParser() {
		chainParser.run {
			parsed("\"foo\"").assertEqualTo(chain(atom(literal("foo"))))
			parsed("\"foo\".bar").assertEqualTo(chain(atom(literal("foo")), "bar"))
			parsed("\"foo\".bar.goo").assertEqualTo(chain(atom(literal("foo")), "bar", "goo"))
			parsed("123").assertEqualTo(chain(atom(literal(123))))
			parsed("123.bar").assertEqualTo(chain(atom(literal(123)), "bar"))
			parsed("foo").assertEqualTo(chain(atom("foo")))
			parsed("foo.bar").assertEqualTo(chain(atom("foo"), "bar"))
		}
	}
}
