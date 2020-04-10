package leo14.lambda2

import leo.base.assertEqualTo
import kotlin.test.Test

class UtilTest {
	@Test
	fun pairUnpair() {
		pair
			.invoke("Hello, ".term)
			.invoke("world!".term)
			.unsafeUnpair
			.assertEqualTo("Hello, ".term to "world!".term)
	}
}