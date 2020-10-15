package leo19.compiler

import leo19.script
import kotlin.test.Test
import kotlin.test.assertFails

class ResolverTest {
	@Test
	fun testEquals_pass() {
		emptyResolver.testEquals(true.script, true.script)
	}

	@Test
	fun testEquals_fail() {
		assertFails {
			emptyResolver.testEquals(true.script, false.script)
		}
	}
}