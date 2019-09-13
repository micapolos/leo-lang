package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.forgetName
import leo13.untyped.replaceName
import leo13.untyped.value.bodyScript
import leo13.untyped.value.value
import kotlin.test.Test

class ReplaceTest {
	@Test
	fun scriptLine() {
		replace(value())
			.scriptLine
			.assertEqualTo(forgetName lineTo script())

		replace(value("foo"))
			.scriptLine
			.assertEqualTo(replaceName lineTo value("foo").bodyScript)
	}
}