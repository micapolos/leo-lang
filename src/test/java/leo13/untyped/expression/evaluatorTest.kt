package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.untyped.value.function
import leo13.untyped.value.item
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
	fun evaluateGet() {
		valueContext().evaluate(
			expression(
				op(
					value(
						"foo" lineTo value(),
						"plus" lineTo value("one" lineTo value("more")))),
				op(get("one"))))
			.assertEqualTo(
				value("one" lineTo value("more")))
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
			.assertEqualTo(value("given" lineTo value("foo")))
	}

	@Test
	fun fix() {
		valueContext()
			.evaluate(
				expression()
					.plus(op(value("zoo")))
					.plus(op(value("function" lineTo value(item(function(
						valueContext(),
						expression(given.op)))))))
					.plus(op(fix(expression(op(value("foo")))))))
			.assertEqualTo(value(
				"given" lineTo value(item(function(valueContext(), expression(given.op)))),
				"given" lineTo value("foo")))
	}

	@Test
	fun recursion() {
		valueContext()
			.evaluate(
				expression(
					op(value("foo")),
					op(
						value("function" lineTo value(
							item(
								function(
									valueContext(),
									expression(
										op(given),
										op(
											switch(
												"foo" caseTo expression(
													op(given),
													op(previous),
													op(fix(expression(op(switched), op(get("foo")), op(content))))),
												"bar" caseTo expression("ok"))))))))),
					op(fix(expression(op(value("foo" lineTo value("foo" lineTo value("bar")))))))))
			.assertEqualTo(value("ok"))
	}
}