package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun sentenceWriter() {
		patternSentenceWriter
			.sentenceLine(pattern(start(choice(either(zeroWord)))))
			.assertEqualTo(
				patternWord lineTo patternStartSentenceWriter.bodySentence(
					start(choice(either(zeroWord)))))
	}

	@Test
	fun contains() {
		pattern(bitWord lineTo pattern(
			choice(
				either(zeroWord),
				either(oneWord))))
			.contains(pattern(bitWord lineTo pattern(zeroWord)))

		pattern(bitWord lineTo pattern(
			choice(
				either(zeroWord),
				either(oneWord))))
			.contains(
				pattern(bitWord lineTo pattern(
					choice(
						either(zeroWord),
						either(oneWord)))))
	}
}