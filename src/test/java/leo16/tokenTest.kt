package leo16

import leo.base.assertContains
import leo.base.assertEqualTo
import leo15.beginName
import leo15.endName
import leo15.tokenName
import kotlin.test.Test

class TokenTest {
	@Test
	fun sentence() {
		"plus".beginToken.sentence.assertEqualTo(tokenName(beginName("plus"())))
		endToken.sentence.assertEqualTo(tokenName(endName()))
	}

	@Test
	fun tokenSeq() {
		"point"("x"("zero"()), "y"("one"()))
			.tokenSeq
			.assertContains(
				"point".beginToken,
				"x".beginToken,
				"zero".beginToken,
				endToken,
				endToken,
				"y".beginToken,
				"one".beginToken,
				endToken,
				endToken,
				endToken)
	}
}