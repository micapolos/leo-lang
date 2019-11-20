package leo14.lambda

import leo.base.assertEqualTo
import leo13.index0
import leo13.index1
import leo13.index2
import leo13.index3
import kotlin.test.Test

class FreeVariableCountTest {
	@Test
	fun test() {
		arg0<Any>().freeVariableCount.assertEqualTo(index1)
		arg1<Any>().freeVariableCount.assertEqualTo(index2)

		fn(arg0<Any>()).freeVariableCount.assertEqualTo(index0)
		fn(arg1<Any>()).freeVariableCount.assertEqualTo(index1)
		fn(arg2<Any>()).freeVariableCount.assertEqualTo(index2)

		fn(fn(arg0<Any>())).freeVariableCount.assertEqualTo(index0)
		fn(fn(arg1<Any>())).freeVariableCount.assertEqualTo(index0)
		fn(fn(arg2<Any>())).freeVariableCount.assertEqualTo(index1)

		arg0<Any>().invoke(arg0()).freeVariableCount.assertEqualTo(index1)
		arg0<Any>().invoke(arg1()).freeVariableCount.assertEqualTo(index2)
		arg0<Any>().invoke(arg2()).freeVariableCount.assertEqualTo(index3)
		arg1<Any>().invoke(arg0()).freeVariableCount.assertEqualTo(index2)
		arg2<Any>().invoke(arg0()).freeVariableCount.assertEqualTo(index3)
		arg1<Any>().invoke(arg1()).freeVariableCount.assertEqualTo(index2)
		arg2<Any>().invoke(arg2()).freeVariableCount.assertEqualTo(index3)
	}
}