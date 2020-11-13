package leo14.matching

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScriptTest {
	@Test
	fun get() {
		script(
			"shape" lineTo script(
				"square" lineTo script(literal(20))))
			.switch(
				"circle" caseTo { it.get("number") },
				"square" caseTo { it.get("number") })
			.assertEqualTo(script(literal(20)))
	}
}
