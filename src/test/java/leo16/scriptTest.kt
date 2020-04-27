package leo16

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun construction() {
		script()
		script("zero"())
		script("zero"(), "plus"("one"()))
		script("zero"()) + "plus"("one"()) + "minus"("two"())
	}

	@Test
	fun string() {
		script("zero"(), "plus"("one"()))
			.toString()
			.assertEqualTo("zero\nplus one")
	}
}