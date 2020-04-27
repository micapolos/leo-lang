package leo16

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
}