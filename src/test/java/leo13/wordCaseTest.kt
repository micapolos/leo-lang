package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class WordCaseTest {
	@Test
	fun sentenceLine() {
		case(zeroWord)
			.sentenceLine
			.assertEqualTo(caseWord lineTo sentence(zeroWord))
	}
}