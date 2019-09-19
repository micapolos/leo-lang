package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.untyped.value.lineTo
import leo13.untyped.value.value
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun evaluate() {
		evaluator()
			.plus(plus("foo" lineTo expression()))
			.assertEqualTo(evaluator().set(value("foo").evaluated))
	}

	@Test
	fun given() {
		val evaluator = valueContext()
			.give(value("zero"))
			.give(value("one"))
			.evaluator()

		evaluator
			.plus(given.op)
			.assertEqualTo(
				evaluator.set(
					evaluated(
						value(
							"given" lineTo value("zero"),
							"given" lineTo value("one")))))
	}

	@Test
	fun switch() {
		evaluator()
			.set(evaluated(value("shape" lineTo value("circle" lineTo value("radius")))))
			.plus(switch("circle" caseTo expression(switched.op)))
			.assertEqualTo(
				evaluator()
					.set(evaluated(value(
						"radius" lineTo value(),
						"times" lineTo value("two")))))
	}
}