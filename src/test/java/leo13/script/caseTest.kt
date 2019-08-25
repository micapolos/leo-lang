package leo13.script

import leo13.assertEqualsToScript
import kotlin.test.Test

class CaseTest {
	@Test
	fun scriptable() {
		("zero" caseTo expr()).assertEqualsToScript("zero(expr(null()))")
	}
}