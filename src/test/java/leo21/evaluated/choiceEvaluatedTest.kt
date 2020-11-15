package leo21.evaluated

import leo.base.assertEqualTo
import leo21.evaluator.emptyChoiceEvaluated
import leo21.evaluator.lineEvaluated
import leo21.evaluator.plusChosen
import leo21.evaluator.plusNotChosen
import leo21.evaluator.switch
import leo21.type.stringLine
import kotlin.test.Test

class ChoiceEvaluatedTest {
	@Test
	fun choice_1() {
		emptyChoiceEvaluated
			.plusNotChosen(stringLine)
			.plusChosen("foo".lineEvaluated)
			.switch({ it }, { it })
			.assertEqualTo("foo".lineEvaluated)
	}

	@Test
	fun choice_2() {
		emptyChoiceEvaluated
			.plusChosen("foo".lineEvaluated)
			.plusNotChosen(stringLine)
			.switch({ it }, { it })
			.assertEqualTo(emptyChoiceEvaluated.plusChosen("foo".lineEvaluated))
	}
}