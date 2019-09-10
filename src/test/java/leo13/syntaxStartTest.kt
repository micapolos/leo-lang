package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class SyntaxStartTest {
	@Test
	fun sentenceStart() {
		start(syntax(given()))
			.sentenceStart
			.assertEqualTo(start(givenWord))
	}
}