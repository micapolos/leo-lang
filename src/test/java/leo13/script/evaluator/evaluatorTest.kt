package leo13.script.evaluator

import leo.base.assertEqualTo
import leo13.given
import leo13.lineTo
import leo13.outside
import leo13.script.*
import leo13.value
import org.junit.Test

class EvaluatorTest {
	@Test
	fun begin() {
		evaluator()
			.assertEqualTo(Evaluator(bindings(), value()))
	}

	private val argumentEvaluator =
		evaluator(
			bindings(
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
		evaluator(bindings(), value("one" lineTo value("rhs" lineTo value())))
			.evaluate(switch)
			.assertEqualTo(
				value(
					"rhs" lineTo value(),
					"jeden" lineTo value()))
	}

	@Test
	fun pushSwitch_secondCase() {
		evaluator(bindings(), value("two" lineTo value("rhs" lineTo value())))
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
		evaluator(bindings(), value("one" lineTo value("rhs" lineTo value())))
			.evaluateOrNull(case)
			.assertEqualTo(value("rhs" lineTo value(), "jeden" lineTo value()))
	}

	@Test
	fun pushCase_mismatch() {
		evaluator(bindings(), value("two" lineTo value("rhs" lineTo value())))
			.evaluateOrNull(case)
			.assertEqualTo(null)
	}
}