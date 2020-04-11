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

	@Test
	fun freeVariableCount() {
		at(0).freeVariableCount.assertEqualTo(1)
		at(1).freeVariableCount.assertEqualTo(2)
		at(2).freeVariableCount.assertEqualTo(3)
		fn(at(0)).freeVariableCount.assertEqualTo(0)
		fn(at(1)).freeVariableCount.assertEqualTo(1)
		fn(at(2)).freeVariableCount.assertEqualTo(2)
		fn(fn(at(2))).freeVariableCount.assertEqualTo(1)
		fn(fn(fn(at(2)))).freeVariableCount.assertEqualTo(0)
		at(0)(at(0)).freeVariableCount.assertEqualTo(1)
		at(0)(at(1)).freeVariableCount.assertEqualTo(2)
		at(1)(at(0)).freeVariableCount.assertEqualTo(2)
		at(1)(at(1)).freeVariableCount.assertEqualTo(2)
	}
}