package leo13.script.evaluator

import leo.base.assertEqualTo
import leo13.argument
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

	private val argumentEvaluator = evaluator()
		.bind(value("one" lineTo value()))
		.bind(value("two" lineTo value()))
		.bind(value("three" lineTo value()))
		.put(value("evaluated" lineTo value()))

	@Test
	fun pushArgument() {
		argumentEvaluator
			.push(argument())
			.assertEqualTo(argumentEvaluator.put(value("three" lineTo value())))
	}

	@Test
	fun pushPreviousArgument() {
		argumentEvaluator
			.push(argument(outside))
			.assertEqualTo(argumentEvaluator.put(value("two" lineTo value())))
	}

	@Test
	fun pushPreviousPreviousArgument() {
		argumentEvaluator
			.push(argument(outside, outside))
			.assertEqualTo(argumentEvaluator.put(value("one" lineTo value())))
	}

	private val switch =
		switch(
			"one" caseTo expr(op("jeden" lineTo expr())),
			"two" caseTo expr(op("dwa" lineTo expr())))

	@Test
	fun pushSwitch_firstCase() {
		evaluator().put(value("one" lineTo value("rhs" lineTo value())))
			.push(switch)
			.assertEqualTo(
				evaluator().put(
					value(
						"rhs" lineTo value(),
						"jeden" lineTo value())))
	}

	@Test
	fun pushSwitch_secondCase() {
		evaluator()
			.put(value("two" lineTo value("rhs" lineTo value())))
			.push(switch)
			.assertEqualTo(
				evaluator()
					.put(
						value(
							"rhs" lineTo value(),
							"dwa" lineTo value())))
	}

	private val case =
		"one" caseTo expr(op("jeden" lineTo expr()))

	@Test
	fun pushCase_match() {
		evaluator()
			.put(value("one" lineTo value("rhs" lineTo value())))
			.pushOrNull(case)
			.assertEqualTo(evaluator().put(value("rhs" lineTo value(), "jeden" lineTo value())))
	}

	@Test
	fun pushCase_mismatch() {
		evaluator()
			.put(value("two" lineTo value("rhs" lineTo value())))
			.pushOrNull(case)
			.assertEqualTo(null)
	}
}