package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class SyntaxCaseTest {
	@Test
	fun sentenceLine() {
		(zeroWord caseTo syntax(start(syntax(oneWord))))
			.sentenceLine
			.assertEqualTo(
				caseWord lineTo sentence(
					zeroWord lineTo sentence(oneWord)))
	}
}
