package leo15.core

import leo.base.assertEqualTo
import kotlin.test.Test

class AndTest {
	@Test
	fun firstSecond() {
		"foo".java
			.and(10.java)
			.run {
				first.assertGives("foo".java)
				second.assertGives(10.java)
			}
	}

	@Test
	fun unsafe() {
		"foo".java
			.and(10.java)
			.run {
				unsafeFirst.assertEqualTo("foo".java)
				unsafeSecond.assertEqualTo(10.java)
			}
	}
}