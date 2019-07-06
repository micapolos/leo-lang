package lambda.lib

import leo.base.assertEqualTo
import kotlin.test.Test

class IntTest {
	@Test
	fun int() {
		13.i32Term.i32Int.assertEqualTo(13)
	}
}