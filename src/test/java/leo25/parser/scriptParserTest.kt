package leo25.parser

import leo.base.assertEqualTo
import leo14.*
import leo25.ValueError
import org.junit.Test
import kotlin.test.assertFailsWith

class ScriptParser {
	@Test
	fun literals() {
		scriptParser.run {
			parsed("123\n").assertEqualTo(script(line(literal(123))))
			parsed("\"123\"\n").assertEqualTo(script(line(literal("123"))))
		}
	}

	@Test
	fun structs() {
		scriptParser.run {
			parsed("").assertEqualTo(script())
			parsed("foo\n").assertEqualTo(script("foo" lineTo script()))
			parsed("foo\nbar\n").assertEqualTo(script("foo" lineTo script(), "bar" lineTo script()))
			parsed("foo\n  bar\n").assertEqualTo(script("foo" lineTo script("bar" lineTo script())))

			parsed("point\n  x\n    10\n  y\n    20\n")
				.assertEqualTo(
					script(
						"point" lineTo script(
							"x" lineTo script(literal(10)),
							"y" lineTo script(literal(20))
						)
					)
				)
		}
	}

	@Test
	fun spacedField() {
		scriptFieldParser.run {
			parsed("foo\n").assertEqualTo("foo" fieldTo script())
			parsed("foo bar\n").assertEqualTo("foo" fieldTo script("bar"))
			parsed("foo bar zoo\n").assertEqualTo("foo" fieldTo script("bar" fieldTo script("zoo")))
			parsed("foo\n  bar\n").assertEqualTo("foo" fieldTo script("bar"))
			parsed("foo\n  bar\n    zoo\n").assertEqualTo("foo" fieldTo script("bar" fieldTo script("zoo")))

			parsed("point\n  x 10\n  y 20\n")
				.assertEqualTo(
					"point" fieldTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20))
					)
				)
		}
	}

	@Test
	fun syntaxError() {
		assertFailsWith<ValueError> {
			"*".scriptOrThrow
		}
	}
}