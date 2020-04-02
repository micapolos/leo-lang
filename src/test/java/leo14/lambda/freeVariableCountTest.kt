package leo14.lambda

import leo.base.assertEqualTo
import kotlin.test.Test

class FreeVariableCountTest {
	@Test
	fun test() {
		arg0<Any>().freeVariableCount.assertEqualTo(1)
		arg1<Any>().freeVariableCount.assertEqualTo(2)

		fn(arg0<Any>()).freeVariableCount.assertEqualTo(0)
		fn(arg1<Any>()).freeVariableCount.assertEqualTo(1)
		fn(arg2<Any>()).freeVariableCount.assertEqualTo(2)

		fn(fn(arg0<Any>())).freeVariableCount.assertEqualTo(0)
		fn(fn(arg1<Any>())).freeVariableCount.assertEqualTo(0)
		fn(fn(arg2<Any>())).freeVariableCount.assertEqualTo(1)

		arg0<Any>().invoke(arg0()).freeVariableCount.assertEqualTo(1)
		arg0<Any>().invoke(arg1()).freeVariableCount.assertEqualTo(2)
		arg0<Any>().invoke(arg2()).freeVariableCount.assertEqualTo(3)
		arg1<Any>().invoke(arg0()).freeVariableCount.assertEqualTo(2)
		arg2<Any>().invoke(arg0()).freeVariableCount.assertEqualTo(3)
		arg1<Any>().invoke(arg1()).freeVariableCount.assertEqualTo(2)
		arg2<Any>().invoke(arg2()).freeVariableCount.assertEqualTo(3)
	}
}