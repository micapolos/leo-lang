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
		val i = m.invokeWithArguments(1, 2) as Integer
		i.assertEqualTo(3)
	}
}