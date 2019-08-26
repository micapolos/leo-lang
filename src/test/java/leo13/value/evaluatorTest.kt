package leo13.value

import leo.base.assertEqualTo
import leo13.given
import leo13.outside
import leo13.script.caseTo
import leo13.script.switch
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
			.evaluate(given())
			.assertEqualTo(value("three" lineTo value()))
	}

	@Test
	fun pushPreviousArgument() {
		argumentEvaluator
			.evaluate(given(outside))
			.assertEqualTo(value("two" lineTo value()))
	}

	@Test
	fun pushPreviousPreviousArgument() {
		argumentEvaluator
			.evaluate(given(outside, outside))
			.assertEqualTo(value("one" lineTo value()))
	}

	private val switch =
		switch(
			"one" caseTo expr(op("jeden" lineTo expr())),
			"two" caseTo expr(op("dwa" lineTo expr())))

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

	private val case =
		"one" caseTo expr(op("jeden" lineTo expr()))

	@Test
	fun pushCase_match() {
		evaluator(valueBindings(), value("one" lineTo value("rhs" lineTo value())))
			.evaluateOrNull(case)
			.assertEqualTo(value("rhs" lineTo value(), "jeden" lineTo value()))
	}

	@Test
	fun pushCase_mismatch() {
		evaluator(valueBindings(), value("two" lineTo value("rhs" lineTo value())))
			.evaluateOrNull(case)
			.assertEqualTo(null)
	}
}