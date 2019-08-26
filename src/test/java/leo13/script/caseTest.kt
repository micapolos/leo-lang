package leo13.script

import leo13.assertEqualsToScript
import leo13.value.expr
import kotlin.test.Test

class CaseTest {
	@Test
	fun scriptable() {
		("zero" caseTo expr()).assertEqualsToScript("zero(expr(null()))")
	}
}