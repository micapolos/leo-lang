package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class NativeInterpreterTest {
	@Test
	fun textAndText() {
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
			plusName lineTo script(leo14.line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun numberSubtractNumber() {
		script(
			leo14.line(literal(5)),
			minusName lineTo script(leo14.line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(2)))
	}

	@Test
	fun numberMultiplyByNumber() {
		script(
			leo14.line(literal(2)),
			timesName lineTo script(leo14.line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(6)))
	}
}