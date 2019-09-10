package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EitherTest {
	@Test
	fun sentenceLine() {
		either(zeroWord)
			.sentenceLine
			.assertEqualTo(eitherWord lineTo sentence(zeroWord))

		(zeroWord eitherTo option(pattern(oneWord)))
			.sentenceLine
			.assertEqualTo(
				eitherWord lineTo sentence(
					zeroWord lineTo pattern(oneWord).bodySentence))
	}

	@Test
	fun failableEither() {
		(eitherWord lineTo sentence(zeroWord))
			.failableEither
			.assertSucceedsWith(either(zeroWord))

		(eitherWord lineTo sentence(zeroWord lineTo pattern(oneWord).bodySentence))
			.failableEither
			.assertSucceedsWith(zeroWord eitherTo option(pattern(oneWord)))

		(poisonWord lineTo sentence(zeroWord lineTo pattern(oneWord).bodySentence))
			.failableEither
			.assertFails

		(eitherWord lineTo sentence(
			xWord lineTo sentence(zeroWord),
			yWord lineTo sentence(oneWord)))
			.failableEither
			.assertFails
	}
}