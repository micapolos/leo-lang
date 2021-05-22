package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import org.junit.Test

class ScriptStringTest {
	@Test
	fun empty() {
		script().string.assertEqualTo("")
	}

	@Test
	fun name() {
		script("foo").string.assertEqualTo("foo\n")
	}

	@Test
	fun simpleField() {
		script("foo" lineTo script("bar")).string.assertEqualTo("foo bar\n")
	}

	@Test
	fun literalField() {
		script("foo" lineTo script(literal("bar"))).string.assertEqualTo("foo \"bar\"\n")
	}

	@Test
	fun complexField() {
		script("foo" lineTo script("bar" lineTo script(), "zoo" lineTo script()))
			.string
			.assertEqualTo("foo\n  bar\n  zoo\n")
	}

	@Test
	fun text() {
		script(literal("foo"))
			.string
			.assertEqualTo("\"foo\"\n")
	}

	@Test
	fun number() {
		script(literal(123))
			.string
			.assertEqualTo("123\n")
	}
}