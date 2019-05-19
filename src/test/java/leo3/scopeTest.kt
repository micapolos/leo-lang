package leo3

import leo.base.assertEqualTo
import leo.base.assertFails
import leo.base.empty
import kotlin.test.Test

class ScopeTest {
	@Test
	fun apply() {
		empty.scope
			.put(value("foo"), template(value("bar")))
			.put(value("zoo"), template(value("zar")))
			.emptyValue
			.apply { call(line("foo")).assertEqualTo(value("bar")) }
			.apply { call(line("zoo")).assertEqualTo(value("zar")) }
			.apply { assertFails { call(line("goo")) } }
	}
}