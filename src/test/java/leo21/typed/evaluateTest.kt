package leo21.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class EvaluateTest {
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