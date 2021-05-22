package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class NativeInterpreterTest {
	@Test
	fun getHash() {
		script(
			"foo" lineTo script(),
			getName lineTo script(hashName lineTo script())
		)
			.interpret
			.assertEqualTo(script(hashName lineTo script(literal(value("foo").hashCode()))))
	}

	@Test
	fun is_() {
		script(
			"foo" lineTo script(),
			isName lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(yesName)))

		script(
			"foo" lineTo script(),
			isName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(noName)))

		script(
			leo14.line(literal("foo")),
			isName lineTo script(leo14.line(literal("foo")))
		)
			.interpret
			.assertEqualTo(script(isName lineTo script(yesName)))
	}

	@Test
	fun textAppendText() {
		script(
			leo14.line(literal("Hello, ")),
			appendName lineTo script(leo14.line(literal("world!")))
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun numberAddNumber() {
		script(
			leo14.line(literal(2)),
			addName lineTo script(leo14.line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun numberSubtractNumber() {
		script(
			leo14.line(literal(5)),
			subtractName lineTo script(leo14.line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(2)))
	}

	@Test
	fun numberMultiplyByNumber() {
		script(
			leo14.line(literal(2)),
			multiplyName lineTo script(byName lineTo script(leo14.line(literal(3))))
		)
			.interpret
			.assertEqualTo(script(literal(6)))
	}
}