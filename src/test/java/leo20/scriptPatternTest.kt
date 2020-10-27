package leo20

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class ScriptPatternTest {
	@Test
	fun any() {
		script("any")
			.patternOrNull
			.assertEqualTo(anyPattern)
	}

	@Test
	fun struct() {
		script(
			"x" lineTo script("any"),
			"y" lineTo script("any"))
			.patternOrNull
			.assertEqualTo(
				pattern(
					"x" fieldTo anyPattern,
					"y" fieldTo anyPattern))
	}
}