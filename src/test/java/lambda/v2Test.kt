package lambda

import lambda.lib.fix
import leo.base.assertEqualTo
import kotlin.test.Test

class V2Test {
	@Test
	fun test() {
		fix.v2.assertEqualTo(null)
	}
}