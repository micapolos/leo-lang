package lambda.lib

import lambda.invoke
import leo.base.assertEqualTo
import kotlin.test.Test

class IdTest {
	@Test
	fun test() {
		id.assertEqualTo(id)

		id(zero).assertEqualTo(zero)
		id(one).assertEqualTo(one)
	}
}