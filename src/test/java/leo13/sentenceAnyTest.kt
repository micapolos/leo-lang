package leo13

import leo.base.assertEqualTo
import org.junit.Test

class SentenceAnyTest {
	@Test
	fun sentenceWriter() {
		sentenceAnySentenceWriter
			.sentenceLine(sentence(any))
			.assertEqualTo(sentenceWord lineTo sentence(anyWord))
	}
}