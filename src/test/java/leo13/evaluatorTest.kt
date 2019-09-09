package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun get() {
		evaluator()
			.plus(
				xWord lineTo sentence(
					pointWord lineTo sentence(
						xWord lineTo sentence(zeroWord),
						yWord lineTo sentence(oneWord))))
			.assertEqualTo(
				evaluator()
					.set(
						script(
							value(
								sentence(xWord lineTo sentence(zeroWord))))))
	}

	@Test
	fun set() {
		evaluator()
			.set(
				script(
					value(
						sentence(
							pointWord lineTo sentence(
								xWord lineTo sentence(zeroWord),
								yWord lineTo sentence(oneWord))))))
			.plus(
				setWord lineTo sentence(xWord lineTo sentence(poisonWord)))
			.assertEqualTo(
				evaluator()
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
		evaluator()
			.plus(setWord lineTo sentence(xWord))
			.assertEqualTo(
				evaluator().plusError(setWord lineTo value(sentence(xWord))))
	}
}