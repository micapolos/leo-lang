package leo16

import leo.base.assertEqualTo
import leo15.dsl.*
import kotlin.test.Test

fun Value.assertGives_(f: F) {
	assertEqualTo(evaluate_(f))
}

class EvalTest {
	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertGives_ { nothing_ }
	}
}