package leo21.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun get_first() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("x")
			.evaluate
			.assertEqualTo(typed("x" lineTo typed(10.0)))
	}

	@Test
	fun get_second() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("y")
			.evaluate
			.assertEqualTo(typed("y" lineTo typed(20.0)))
	}

	@Test
	fun doublePlus() {
		typed(10.0)
			.doublePlus(typed(20.0))
			.evaluate
			.assertEqualTo(typed(30.0))
	}

	@Test
	fun doubleMinus() {
		typed(30.0)
			.doubleMinus(typed(20.0))
			.evaluate
			.assertEqualTo(typed(10.0))
	}

	@Test
	fun doubleTimes() {
		typed(10.0)
			.doubleTimes(typed(20.0))
			.evaluate
			.assertEqualTo(typed(200.0))
	}

	@Test
	fun stringPlus() {
		typed("Hello, ")
			.stringPlus(typed("world!"))
			.evaluate
			.assertEqualTo(typed("Hello, world!"))
	}
}