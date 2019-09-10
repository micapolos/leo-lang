package leo13

import kotlin.test.Test

class WordChoiceTest {
	@Test
	fun sentenceWriter() {
		wordChoiceSentenceWriter
			.sentenceLine(
				choice(
					either(zeroWord),
					either(oneWord)))
			.assertEqualTo(
				choiceWord lineTo sentence(
					wordEitherSentenceWriter.sentenceLine(either(zeroWord)),
					wordEitherSentenceWriter.sentenceLine(either(oneWord))))
	}
}