package leo14.lambda2

import leo.base.assertEqualTo
import kotlin.test.Test

class UtilTest {
	@Test
	fun pairUnpair() {
		pair
			.invoke("Hello, ".valueTerm)
			.invoke("world!".valueTerm)
			.unsafeUnpair
			.assertEqualTo("Hello, ".valueTerm to "world!".valueTerm)
	}
}