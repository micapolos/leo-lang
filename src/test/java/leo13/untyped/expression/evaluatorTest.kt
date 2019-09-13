package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.untyped.value.lineTo
import leo13.untyped.value.value
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun evaluate() {
		evaluator()
			.plus(replace(value("foo")))
			.assertEqualTo(evaluator().set(value("foo")))
	}

	@Test
	fun given() {
		val evaluator = given(value("zero"), value("one")).evaluator()

		evaluator
			.plus(given.op)
			.assertEqualTo(
				evaluator.set(
					value(
						"given" lineTo value("zero"),
						"given" lineTo value("one")))
			)

		evaluator
			.plus(given.op)
			.plus(previous.op)
			.plus(get("zero").op)
			.assertEqualTo(evaluator.set(value("zero")))
	}
}