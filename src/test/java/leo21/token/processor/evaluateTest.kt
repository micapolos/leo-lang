package leo21.token.processor

import leo.base.assertEqualTo
import leo15.dsl.*
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun define() {
		processor {
			x { number(10) }
			y { number(20) }
			z { number(30) }
		}.assertEqualTo(null)
	}
}