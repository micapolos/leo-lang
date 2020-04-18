package leo15.core

import leo.base.assertEqualTo
import kotlin.test.Test

class JavaTest {
	@Test
	fun scriptJava() {
		"foo".java.unsafeValue.assertEqualTo("foo")
	}
}