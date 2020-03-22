package leo14.untyped

import leo.base.assertEqualTo
import org.junit.Test

class NativeTest {
	@Test
	fun classes() {
		Integer(1).javaClass.forInvoke.assertEqualTo(Integer.TYPE)
	}
}