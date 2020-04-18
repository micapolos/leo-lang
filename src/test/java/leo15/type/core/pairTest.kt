package leo15.type.core

import leo.base.assertEqualTo
import leo15.core.anyJava
import leo15.core.pairTo
import kotlin.test.Test

class PairTest {
	@Test
	fun pairTo() {
		"x".anyJava
			.pairTo(10.anyJava)
			.first
			.assertEqualTo("x".anyJava)
	}
}