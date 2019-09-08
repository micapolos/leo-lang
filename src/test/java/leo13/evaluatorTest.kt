package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun get() {
		evaluator()
			.plus(
				xWord lineTo value(
					sentence(
						pointWord lineTo sentence(
							xWord lineTo sentence(zeroWord),
							yWord lineTo sentence(oneWord))),
					pattern(
						pointWord lineTo pattern(
							xWord lineTo pattern(zeroWord),
							yWord lineTo pattern(oneWord)))))
			.assertEqualTo(
				evaluator().set(
					script(
						value(
							sentence(xWord lineTo sentence(zeroWord)),
							pattern(xWord lineTo pattern(zeroWord))))))
	}

	@Test
	fun set() {
		evaluator(
			script(
				value(
					sentence(
						pointWord lineTo sentence(
							xWord lineTo sentence(zeroWord),
							yWord lineTo sentence(oneWord))),
					pattern(
						pointWord lineTo pattern(
							xWord lineTo pattern(zeroWord),
							yWord lineTo pattern(oneWord))))))
			.plus(
				setWord lineTo value(
					sentence(xWord lineTo sentence(poisonWord)),
					pattern(xWord lineTo pattern(poisonWord))))
			.assertEqualTo(
				evaluator(
					script(
						value(
							sentence(
								pointWord lineTo sentence(
									xWord lineTo sentence(poisonWord),
									yWord lineTo sentence(oneWord))),
							pattern(
								pointWord lineTo pattern(
									xWord lineTo pattern(poisonWord),
									yWord lineTo pattern(oneWord)))))))
	}
}