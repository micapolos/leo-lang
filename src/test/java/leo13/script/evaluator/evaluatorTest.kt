package leo13.script.evaluator

import leo.base.assertEqualTo
import leo13.*
import leo13.script.*
import org.junit.Test

class EvaluatorTest {
	@Test
	fun begin() {
		evaluator
			.begin
			.assertEqualTo(Evaluator(bindings(), script()))
	}

	private val argumentEvaluator = evaluator.begin
		.bind(script("one" lineTo script()))
		.bind(script("two" lineTo script()))
		.bind(script("three" lineTo script()))
		.put(script("evaluated" lineTo script()))

	@Test
	fun pushArgument() {
		argumentEvaluator
			.push(argument())
			.assertEqualTo(argumentEvaluator.put(script("three" lineTo script())))
	}

	@Test
	fun pushPreviousArgument() {
		argumentEvaluator
			.push(argument(previous))
			.assertEqualTo(argumentEvaluator.put(script("two" lineTo script())))
	}

	@Test
	fun pushPreviousPreviousArgument() {
		argumentEvaluator
			.push(argument(previous, previous))
			.assertEqualTo(argumentEvaluator.put(script("one" lineTo script())))
	}

	private val switch =
		switch(
			"one" caseTo expr(op("jeden" lineTo expr())),
			"two" caseTo expr(op("dwa" lineTo expr())))

	@Test
	fun pushSwitch_firstCase() {
		evaluator.begin.put(script("one" lineTo script("rhs" lineTo script())))
			.push(switch)
			.assertEqualTo(
				evaluator.begin.put(
					script(
						"rhs" lineTo script(),
						"jeden" lineTo script())))
	}

	@Test
	fun pushSwitch_secondCase() {
		evaluator.begin
			.put(script("two" lineTo script("rhs" lineTo script())))
			.push(switch)
			.assertEqualTo(
				evaluator.begin.put(
					script(
						"rhs" lineTo script(),
						"dwa" lineTo script())))
	}

	private val case =
		"one" caseTo expr(op("jeden" lineTo expr()))

	@Test
	fun pushCase_match() {
		evaluator.begin
			.put(script("one" lineTo script("rhs" lineTo script())))
			.pushOrNull(case)
			.assertEqualTo(evaluator.begin.put(script("rhs" lineTo script(), "jeden" lineTo script())))
	}

	@Test
	fun pushCase_mismatch() {
		evaluator.begin
			.put(script("two" lineTo script("rhs" lineTo script())))
			.pushOrNull(case)
			.assertEqualTo(null)
	}
}