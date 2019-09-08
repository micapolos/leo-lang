package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import kotlin.test.Test

class PatternRuleDynamicTest {
	@Test
	fun writer() {
		patternRuleDynamicWriter
			.scriptLine(dynamic(script))
			.assertEqualTo("dynamic" lineTo script("script"))
	}

	@Test
	fun reader() {
		patternRuleDynamicReader
			.unsafeValue("dynamic" lineTo script("script"))
			.assertEqualTo(dynamic(script))
	}
}