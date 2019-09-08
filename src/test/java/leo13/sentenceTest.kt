package leo13

import leo.base.assertEqualTo
import leo13.base.linesString
import kotlin.test.Test

class SentenceTest {
	@Test
	fun string() {
		sentence(
			"vec" plus sentence(
				"x" plus sentence("zero"),
				"y" plus sentence("one")),
			"plus" plus sentence(
				"vec" plus sentence(
					"x" plus sentence("two"),
					"y" plus sentence("three"))))
			.toString()
			.assertEqualTo(
				linesString(
					"vec",
					"\tx: zero",
					"\ty: one",
					"plus: vec",
					"\tx: two",
					"\ty: three"))
	}

	val xSentenceLine = xWord lineTo sentence(zeroWord)
	val ySentenceLine = yWord lineTo sentence(oneWord)
	val pointSentence = sentence(pointWord lineTo sentence(xSentenceLine, ySentenceLine))

	@Test
	fun getOrNull() {
		pointSentence
			.getOrNull(xWord)
			.assertEqualTo(sentence(xSentenceLine))

		pointSentence
			.getOrNull(yWord)
			.assertEqualTo(sentence(ySentenceLine))

		pointSentence
			.getOrNull(zWord)
			.assertEqualTo(null)
	}

	@Test
	fun setOrNull() {
		pointSentence
			.setOrNull(xWord lineTo sentence(poisonWord))
			.assertEqualTo(
				sentence(
					pointWord lineTo sentence(
						xWord lineTo sentence(poisonWord),
						yWord lineTo sentence(oneWord))))

		pointSentence
			.setOrNull(yWord lineTo sentence(poisonWord))
			.assertEqualTo(
				sentence(
					pointWord lineTo sentence(
						xWord lineTo sentence(zeroWord),
						yWord lineTo sentence(poisonWord))))

		pointSentence
			.setOrNull(zWord lineTo sentence(poisonWord))
			.assertEqualTo(null)
	}
}