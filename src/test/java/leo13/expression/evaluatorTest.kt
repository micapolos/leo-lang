package leo13.expression

import leo.base.assertEqualTo
import leo13.*
import leo13.value.*
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
	fun evaluateEquals() {
		valueContext()
			.evaluate(expression(op(value("zoo")), op(leo13.expression.equals(expression("zoo")))))
			.assertEqualTo(value(booleanName lineTo value(trueName)))

		valueContext()
			.evaluate(expression(op(value("zoo")), op(leo13.expression.equals(expression("bar")))))
			.assertEqualTo(value(booleanName lineTo value(falseName)))
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
						matchingName lineTo value("circle" lineTo value("radius"))))))
	}

	@Test
	fun switchError() {
		assertFails {
			evaluator()
				.set(evaluated(value("shape" lineTo value("rectangle" lineTo value("side")))))
				.plus(switch("circle" caseTo expression(switched.op)))
		}
	}

	@Test
	fun apply() {
		valueContext()
			.evaluate(
				expression()
					.plus(op(value("zoo")))
					.plus("process" lineTo expression(op(value(item(function(
						valueContext(),
						expression(given.op)))))))
					.plus(op(apply(expression(op(value("foo")))))))
			.assertEqualTo(value("given" lineTo value("foo")))
	}

	@Test
	fun applyNative() {
		evaluator()
			.set(evaluated(value(functionName lineTo booleanNotFunctionNativeValue)))
			.plus(apply(expression(op(false.nativeValue))))
			.assertEqualTo(evaluator().set(evaluated(true.nativeValue)))
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