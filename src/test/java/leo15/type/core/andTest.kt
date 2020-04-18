package leo15.type.core

import leo.base.assertEqualTo
import leo15.core.and
import leo15.core.anyJava
import kotlin.test.Test

class AndTest {
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