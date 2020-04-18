package leo15.type.core

import leo.base.assertEqualTo
import leo15.core.java
import kotlin.test.Test

class JavaTest {
	@Test
	fun scriptJava() {
		"foo".java.unsafeValue.assertEqualTo("foo")
	}
}