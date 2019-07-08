package lambda.lib

import lambda.invoke
import leo.base.assertEqualTo
import kotlin.test.Test

class IntTest {
	@Test
	fun int() {
		13.i32Term.i32Int.assertEqualTo(13)
		13.i32Term.i32Eq(13.i32Term).assertEqualTo(oneBit)
		13.i32Term.i32Eq(14.i32Term).assertEqualTo(zeroBit)
	}
}