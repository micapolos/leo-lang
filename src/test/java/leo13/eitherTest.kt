package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EitherTest {
	@Test
	fun sentenceLine() {
		(zeroWord eitherTo pattern(oneWord))
			.sentenceLine
			.assertEqualTo(
				eitherWord lineTo sentence(
					zeroWord lineTo pattern(oneWord).bodySentence))
	}

	@Test
	fun failableEither() {
		(eitherWord lineTo sentence(zeroWord lineTo pattern(oneWord).bodySentence))
			.failableEither
			.assertSucceedsWith(zeroWord eitherTo pattern(oneWord))

		(poisonWord lineTo sentence(zeroWord lineTo pattern(oneWord).bodySentence))
			.failableEither
			.assertFails

		(eitherWord lineTo sentence(zeroWord))
			.failableEither
			.assertFails
	}
}