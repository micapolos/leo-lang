package leo5

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ScriptTest {
	@Test
	fun isEmpty() {
		script(empty).isEmpty.assertEqualTo(true)
		script(line("empty")).isEmpty.assertEqualTo(false)
	}

	@Test
	fun simpleLineOrNull() {
		script(empty).simpleLineOrNull.assertEqualTo(null)
		script(line("one")).simpleLineOrNull.assertEqualTo(line("one"))
		script(line("two"), line("two")).simpleLineOrNull.assertEqualTo(null)
	}
}