package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class GivenTest {
	@Test
	fun sentenceStart() {
		given()
			.sentenceStart
			.assertEqualTo(start(givenWord))

		given(previous, previous)
			.sentenceStart
			.assertEqualTo(
				start(
					givenWord lineTo sentence(
						previousWord lineTo sentence(previousWord))))
	}
}