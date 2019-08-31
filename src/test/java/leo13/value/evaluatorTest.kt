package leo13.value

import leo.base.assertEqualTo
import leo13._this
import leo13.wrap
import org.junit.Test

class EvaluatorTest {
	@Test
	fun begin() {
		evaluator()
			.assertEqualTo(Evaluator(valueBindings(), value()))
	}

	private val argumentEvaluator =
		evaluator(
			valueBindings(
				value("one" lineTo value()),
				value("two" lineTo value()),
				value("three" lineTo value())),
			value("evaluated" lineTo value()))

	@Test
	fun pushArgument() {
		argumentEvaluator
			.evaluate(expr(given()))
			.assertEqualTo(value("three" lineTo value()))
	}

	@Test
	fun pushPreviousArgument() {
		argumentEvaluator
			.evaluate(expr(given(outside)))
			.assertEqualTo(value("two" lineTo value()))
	}

	@Test
	fun pushPreviousPreviousArgument() {
		argumentEvaluator
			.evaluate(expr(given(outside, outside)))
			.assertEqualTo(value("one" lineTo value()))
	}

	private val switch =
		switch(
			"one" caseTo expr(_this, op("jeden" lineTo expr())),
			"two" caseTo expr(_this, op("dwa" lineTo expr())))

	@Test
	fun pushSwitch_firstCase() {
		evaluator(valueBindings(), value("one" lineTo value("rhs" lineTo value())))
			.evaluate(switch)
			.assertEqualTo(
				value(
					"rhs" lineTo value(),
					"jeden" lineTo value()))
	}

	@Test
	fun pushSwitch_secondCase() {
		evaluator(valueBindings(), value("two" lineTo value("rhs" lineTo value())))
			.evaluate(switch)
			.assertEqualTo(
				value(
					"rhs" lineTo value(),
					"dwa" lineTo value()))
	}

	@Test
	fun pushCase_match() {
		evaluator(valueBindings(), value("one" lineTo value("rhs" lineTo value())))
			.evaluateOrNull("one" caseTo expr(_this, op("jeden" lineTo expr())))
			.assertEqualTo(value("rhs" lineTo value(), "jeden" lineTo value()))
	}

	@Test
	fun pushCase_mismatch() {
		evaluator(valueBindings(), value("two" lineTo value("rhs" lineTo value())))
			.evaluateOrNull("one" caseTo expr(_this, op("jeden" lineTo expr())))
			.assertEqualTo(null)
	}

	@Test
	fun evaluateWrap() {
		evaluator(valueBindings(), value("red" lineTo value(), "color" lineTo value()))
			.evaluate(wrap)
			.assertEqualTo(value("color" lineTo value("red")))
	}

	@Test
	fun evaluateCall() {
		evaluator(
			valueBindings(value("outside")),
			value(
				fn(
					valueBindings(value("inside")),
					expr(
						value(),
						op("argument" lineTo expr(given())),
						op("closure" lineTo expr(given(outside)))))))
			.evaluate(
				call(
					expr(
						given(),
						op("parameter" lineTo expr()))))
			.assertEqualTo(
				value(
					"argument" lineTo value(
						"outside" lineTo value(),
						"parameter" lineTo value()),
					"closure" lineTo value(
						"inside" lineTo value())))
	}
}