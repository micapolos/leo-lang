package leo14.lambda

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun native() {
		term("foo").eval.assertEqualTo("foo")
	}

	@Test
	fun id() {
		id<String>().invoke(term("foo")).eval.assertEqualTo("foo")
	}
}