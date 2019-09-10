package leo13

import leo.base.assertEqualTo
import org.junit.Test

class PatternStartTest {
	@Test
	fun sentenceWriter() {
		patternStartSentenceWriter
			.sentenceLine(start(choice(either(zeroWord))))
			.assertEqualTo(
				startWord lineTo sentence(
					wordChoiceSentenceWriter.sentenceLine(choice(either(zeroWord)))))
	}
}