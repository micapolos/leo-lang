package leo13.value

import leo13.script.assertEqualsToScript
import kotlin.test.Test

class CaseTest {
	@Test
	fun scriptable() {
		("zero" caseTo expr()).assertEqualsToScript("zero(expr(null()))")
	}
}