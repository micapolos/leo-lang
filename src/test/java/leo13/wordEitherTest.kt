package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class WordEitherTest {
	@Test
	fun sentenceWriter() {
		wordEitherSentenceWriter
			.sentenceLine(either(zeroWord))
			.assertEqualTo(eitherWord lineTo sentence(zeroWord))
	}
}