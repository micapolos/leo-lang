package leo.java.lang.reflect

import leo.base.assertEqualTo
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import kotlin.test.Test

class MethodHandlesTest {
	@Test
	fun test() {
		val m = MethodHandles
			.lookup()
			.findStatic(
				Integer::class.java,
				"sum",
				MethodType.methodType(Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType))
		val i = m.invokeExact(Integer.valueOf(1), Integer.valueOf(2)) as Integer
		i.assertEqualTo(null)
	}
}