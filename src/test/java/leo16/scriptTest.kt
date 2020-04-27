package leo16

import kotlin.test.Test

class ScriptTest {
	@Test
	fun construction() {
		script()
		script("zero"())
		script("zero"(), "plus"("one"()))
		script("zero"()) + "plus"("one"()) + "minus"("two"())
	}
}