package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun get() {
		val evaluator = evaluator()
		evaluator
			.plus(
				xWord lineTo sentence(
					pointWord lineTo sentence(
						xWord lineTo sentence(zeroWord),
						yWord lineTo sentence(oneWord))),
				evaluator.evaluateFn)
			.assertEqualTo(
				evaluator
					.set(
						script(
							value(
								sentence(xWord lineTo sentence(zeroWord))))))
	}

	@Test
	fun set() {
		val evaluator = evaluator()
			.set(
				script(
					value(
						sentence(
							pointWord lineTo sentence(
								xWord lineTo sentence(zeroWord),
								yWord lineTo sentence(oneWord))))))
		evaluator
			.plus(
				setWord lineTo sentence(xWord lineTo sentence(poisonWord)),
				evaluator.evaluateFn)
			.assertEqualTo(
				evaluator
					.set(
						script(
							value(
								sentence(
									pointWord lineTo sentence(
										xWord lineTo sentence(poisonWord),
										yWord lineTo sentence(oneWord)))))))
	}

	@Test
	fun setError() {
		val evaluator = evaluator()
		evaluator
			.plus(setWord lineTo sentence(xWord), evaluator.evaluateFn)
			.assertEqualTo(
				evaluator.plusError(setWord lineTo value(sentence(xWord))))
	}
}