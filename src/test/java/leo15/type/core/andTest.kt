package leo15.type.core

import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import leo15.core.and
import leo15.core.anyJava
import leo15.core.java
import kotlin.test.Test

class AndTest {
	@Test
	fun equality() {
		10.java.and("foo".java)
			.assertEqualTo(10.java.and("foo".java))
		10.java.and("foo".java)
			.assertNotEqualTo(10.java.and("bar".java))
		10.java.and("foo".java)
			.assertNotEqualTo(20.java.and("foo".java))
	}

	@Test
	fun pairTo() {
		"foo".anyJava
			.and(10.anyJava)
			.run {
				first.assertEqualTo("foo".anyJava)
				second.assertEqualTo(10.anyJava)
			}
	}
}