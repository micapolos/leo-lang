package leo14.untyped.pretty

import leo.base.assertEqualTo
import leo14.leonardoScript
import kotlin.test.Test

class PrettyTest {
	@Test
	fun leonardo() {
		leonardoScript.prettyString.assertEqualTo("")
	}
}