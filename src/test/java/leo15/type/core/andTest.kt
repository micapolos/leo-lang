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
			.first
			.assertEqualTo("foo".anyJava)

		"foo".anyJava
			.and(10.anyJava)
			.second
			.assertEqualTo(10.anyJava)
	}
}