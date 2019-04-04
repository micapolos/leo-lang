package leo32.interpreter

import leo.base.assertEqualTo
import leo32.runtime.term
import kotlin.test.Test

class EvalTest {
	@Test
	fun test() {
		eval(term("one"))
			.assertEqualTo(term("one"))
	}
}