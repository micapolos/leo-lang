package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun sentenceLine() {
		choice()
			.sentenceLine
			.assertEqualTo(choiceWord lineTo sentence(noneWord))

		choice(
			zeroWord eitherTo pattern(oneWord),
			oneWord eitherTo pattern(zeroWord))
			.sentenceLine
			.assertEqualTo(
				choiceWord lineTo sentence(
					(zeroWord eitherTo pattern(oneWord)).sentenceLine,
					(oneWord eitherTo pattern(zeroWord)).sentenceLine))
	}

	@Test
	fun failableChoice() {
		(choiceWord lineTo sentence(noneWord))
			.failableChoice
			.assertEqualTo(success(choice()))

		(choiceWord lineTo sentence(
			(zeroWord eitherTo pattern(oneWord)).sentenceLine,
			(oneWord eitherTo pattern(zeroWord)).sentenceLine))
			.failableChoice
			.assertSucceedsWith(
				choice(
					zeroWord eitherTo pattern(oneWord),
					oneWord eitherTo pattern(zeroWord)))
	}
}