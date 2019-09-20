package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.untyped.value.function
import leo13.untyped.value.item
import leo13.untyped.value.lineTo
import leo13.untyped.value.value
import kotlin.test.Test
import kotlin.test.assertFails

class EvaluatorTest {
	@Test
	fun evaluate() {
		evaluator()
			.plus(plus("foo" lineTo expression()))
			.assertEqualTo(evaluator().set(value("foo").evaluated))
	}

	@Test
	fun evaluateGet() {
		valueContext().evaluate(
			expression(
				op(value("foo" lineTo value(), "plus" lineTo value("one" lineTo value("more")))),
				op(get("one"))))
			.assertEqualTo(
				value(
					"foo" lineTo value(),
					"one" lineTo value("more")))
	}

	@Test
	fun given() {
		valueContext()
			.give(value("zero"))
			.give(value("one"))
			.evaluate(
				expression(
					op(value("foo" lineTo value())),
					op(given)))
			.assertEqualTo(
				value(
					"foo" lineTo value(),
					"given" lineTo value("zero"),
					"given" lineTo value("one")))
	}

	@Test
	fun switch() {
		evaluator()
			.set(evaluated(value("shape" lineTo value("circle" lineTo value("radius")))))
			.plus(switch("circle" caseTo expression(switched.op)))
			.assertEqualTo(
				evaluator()
					.set(evaluated(value(
						"switched" lineTo value("circle" lineTo value("radius"))))))
	}

	@Test
	fun apply() {
		valueContext()
			.evaluate(
				expression()
					.plus(op(value("zoo")))
					.plus(op(value("function" lineTo value(item(function(
						valueContext(),
						expression(given.op)))))))
					.plus(op(apply(expression(op(value("foo")))))))
			.assertEqualTo(value("zoo" lineTo value(), "given" lineTo value("foo")))
	}

	@Test
	fun recursionStackOverflow() {
		assertFails {
			val expression =
				expression(
					op(given),
					op(get("function")),
					op(
						apply(
							expression(
								op(given),
								op(get("function")),
								op(given),
								op(get("value"))))))

			valueContext()
				.give(
					value(
						"function" lineTo value(item(function(valueContext(), expression))),
						"value" lineTo value("foo")))
				.evaluate(expression)
		}
	}
}