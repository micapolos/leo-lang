package leo13

import leo.base.assertEqualTo
import leo13.base.linesString
import kotlin.test.Test

class SentenceTest {
	@Test
	fun string() {
		sentence(
			pointWord lineTo sentence(
				xWord lineTo sentence(zeroWord),
				yWord lineTo sentence(oneWord)),
			plusWord lineTo sentence(
				pointWord lineTo sentence(
					xWord lineTo sentence(oneWord),
					yWord lineTo sentence(zeroWord))))
			.toString()
			.assertEqualTo(
				linesString(
					"point",
					"\tx: zero",
					"\ty: one",
					"plus: point",
					"\tx: one",
					"\ty: zero"))
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