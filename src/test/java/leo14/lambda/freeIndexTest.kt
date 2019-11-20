package leo14.lambda

import leo.base.assertEqualTo
import leo13.index0
import leo13.index1
import leo13.index2
import kotlin.test.Test

class FreeIndexTest {
	@Test
	fun test() {
		arg0<Any>().freeIndexOrNull.assertEqualTo(index0)
		arg1<Any>().freeIndexOrNull.assertEqualTo(index1)

		fn(arg0<Any>()).freeIndexOrNull.assertEqualTo(null)
		fn(arg1<Any>()).freeIndexOrNull.assertEqualTo(index0)
		fn(arg2<Any>()).freeIndexOrNull.assertEqualTo(index1)

		fn(fn(arg0<Any>())).freeIndexOrNull.assertEqualTo(null)
		fn(fn(arg1<Any>())).freeIndexOrNull.assertEqualTo(null)
		fn(fn(arg2<Any>())).freeIndexOrNull.assertEqualTo(index0)

		arg0<Any>().invoke(arg0()).freeIndexOrNull.assertEqualTo(index0)
		arg0<Any>().invoke(arg1()).freeIndexOrNull.assertEqualTo(index1)
		arg0<Any>().invoke(arg2()).freeIndexOrNull.assertEqualTo(index2)
		arg1<Any>().invoke(arg0()).freeIndexOrNull.assertEqualTo(index1)
		arg2<Any>().invoke(arg0()).freeIndexOrNull.assertEqualTo(index2)
		arg1<Any>().invoke(arg1()).freeIndexOrNull.assertEqualTo(index1)
		arg2<Any>().invoke(arg2()).freeIndexOrNull.assertEqualTo(index2)
	}
}