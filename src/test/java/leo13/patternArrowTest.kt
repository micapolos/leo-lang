package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternArrowTest {
	@Test
	fun sentenceWriter() {
		patternArrowSentenceWriter
			.sentenceLine(pattern(zeroWord) arrowTo pattern(oneWord))
			.assertEqualTo(null)
	}
}