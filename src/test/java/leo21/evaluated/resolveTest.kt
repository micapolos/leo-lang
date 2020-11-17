package leo21.evaluated

import leo.base.assertEqualTo
import leo21.evaluator.evaluated
import leo21.evaluator.lineEvaluated
import leo21.evaluator.lineTo
import leo21.evaluator.resolve
import kotlin.math.cos
import kotlin.math.sin
import kotlin.test.Test

class ResolveTest {
	@Test
	fun get() {
		evaluated(
			"point" lineTo evaluated(
				"x" lineTo evaluated(10.0),
				"y" lineTo evaluated(20.0)),
			"x" lineTo evaluated())
			.resolve
			.assertEqualTo(evaluated("x" lineTo evaluated(10.0)))
	}

	@Test
	fun make() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0),
			"point" lineTo evaluated())
			.resolve
			.assertEqualTo(
				evaluated(
					"point" lineTo evaluated(
						"x" lineTo evaluated(10.0),
						"y" lineTo evaluated(20.0))))
	}

	@Test
	fun doublePlusDouble() {
		evaluated(
			10.0.lineEvaluated,
			"plus" lineTo evaluated(20.0))
			.resolve
			.assertEqualTo(evaluated(30.0))
	}

	@Test
	fun doubleMinusDouble() {
		evaluated(
			30.0.lineEvaluated,
			"minus" lineTo evaluated(20.0))
			.resolve
			.assertEqualTo(evaluated(10.0))
	}

	@Test
	fun doubleTimesDouble() {
		evaluated(
			10.0.lineEvaluated,
			"times" lineTo evaluated(20.0))
			.resolve
			.assertEqualTo(evaluated(200.0))
	}

	@Test
	fun stringPlusString() {
		evaluated(
			"Hello, ".lineEvaluated,
			"plus" lineTo evaluated("world!"))
			.resolve
			.assertEqualTo(evaluated("Hello, world!"))
	}

	@Test
	fun sinus() {
		evaluated(
			1.0.lineEvaluated,
			"sinus" lineTo evaluated())
			.resolve
			.assertEqualTo(evaluated(sin(1.0)))
	}

	@Test
	fun cosinus() {
		evaluated(
			1.0.lineEvaluated,
			"cosinus" lineTo evaluated())
			.resolve
			.assertEqualTo(evaluated(cos(1.0)))
	}
}