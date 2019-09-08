package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun wordSentenceLine() {
		pattern(zeroWord)
			.sentenceLine
			.assertEqualTo(patternWord lineTo sentence(zeroWord))
	}

	@Test
	fun lineSentenceLine() {
		pattern(zeroWord lineTo pattern(oneWord))
			.sentenceLine
			.assertEqualTo(patternWord lineTo sentence(
				zeroWord lineTo sentence(oneWord)))
	}

	@Test
	fun linkSentenceLine() {
		pattern(
			zeroWord lineTo pattern(oneWord),
			oneWord lineTo pattern(zeroWord))
			.sentenceLine
			.assertEqualTo(patternWord lineTo sentence(
				zeroWord lineTo sentence(oneWord),
				oneWord lineTo sentence(zeroWord)))
	}

	@Test
	fun choiceSentenceLine() {
		pattern(
			choice())
			.sentenceLine
			.assertEqualTo(patternWord lineTo sentence(choice().sentenceLine))
	}

	@Test
	fun sentenceSentenceLine() {
		pattern(sentence)
			.sentenceLine
			.assertEqualTo(patternWord lineTo sentence(sentenceWord))
	}

	@Test
	fun arrowSentenceLine() {
		// TODO()
	}

	@Test
	fun wordFailablePattern() {
		(patternWord lineTo sentence(zeroWord))
			.failablePattern
			.assertSucceedsWith(pattern(zeroWord))
	}

	@Test
	fun lineFailablePattern() {
		(patternWord lineTo sentence(zeroWord lineTo sentence(oneWord)))
			.failablePattern
			.assertSucceedsWith(pattern(zeroWord lineTo pattern(oneWord)))
	}

	@Test
	fun linkFailablePattern() {
		(patternWord lineTo sentence(
			zeroWord lineTo sentence(oneWord),
			oneWord lineTo sentence(zeroWord)))
			.failablePattern
			.assertSucceedsWith(pattern(
				zeroWord lineTo pattern(oneWord),
				oneWord lineTo pattern(zeroWord)))
	}

	@Test
	fun choiceFailablePattern() {
		(patternWord lineTo sentence(choice().sentenceLine))
			.failablePattern
			.assertSucceedsWith(pattern(choice()))
	}

	@Test
	fun sentenceFailablePattern() {
		(patternWord lineTo sentence(sentenceWord))
			.failablePattern
			.assertSucceedsWith(pattern(sentence))
	}
}