package leo21.evaluated

import leo.base.assertEqualTo
import leo.base.assertNull
import leo21.evaluator.accessOrNull
import leo21.evaluator.emptyChoiceEvaluated
import leo21.evaluator.evaluated
import leo21.evaluator.lineTo
import leo21.evaluator.plusChosen
import leo21.evaluator.plusNotChosen
import leo21.type.numberType
import leo21.type.lineTo
import kotlin.test.Test

class AccessTest {
	@Test
	fun struct_match1() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.accessOrNull("x")
			.assertEqualTo(evaluated("x" lineTo evaluated(10.0)))
	}

	@Test
	fun struct_match2() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.accessOrNull("y")
			.assertEqualTo(evaluated("y" lineTo evaluated(20.0)))
	}

	@Test
	fun struct_mismatch() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.accessOrNull("z")
			.assertNull
	}

	@Test
	fun choice_match1() {
		emptyChoiceEvaluated
			.plusChosen("x" lineTo evaluated(10.0))
			.plusNotChosen("y" lineTo numberType)
			.evaluated
			.accessOrNull("x")
			.assertEqualTo(evaluated("x" lineTo evaluated(10.0)))
	}

	@Test
	fun choice_match2() {
		emptyChoiceEvaluated
			.plusNotChosen("x" lineTo numberType)
			.plusChosen("y" lineTo evaluated(20.0))
			.evaluated
			.accessOrNull("y")
			.assertEqualTo(evaluated("y" lineTo evaluated(20.0)))
	}

	@Test
	fun choice_mismatch1() {
		emptyChoiceEvaluated
			.plusChosen("x" lineTo evaluated(10.0))
			.plusNotChosen("y" lineTo numberType)
			.evaluated
			.accessOrNull("y")
			.assertNull
	}

	@Test
	fun choice_mismatch2() {
		emptyChoiceEvaluated
			.plusNotChosen("x" lineTo numberType)
			.plusChosen("y" lineTo evaluated(20.0))
			.evaluated
			.accessOrNull("x")
			.assertNull
	}

	@Test
	fun choice_mismatch() {
		emptyChoiceEvaluated
			.plusNotChosen("x" lineTo numberType)
			.plusChosen("y" lineTo evaluated(20.0))
			.evaluated
			.accessOrNull("z")
			.assertNull
	}
}