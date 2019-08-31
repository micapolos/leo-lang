package leo13.value

import leo13._this
import leo13.script.assertEqualsToScript
import kotlin.test.Test

class CaseTest {
	@Test
	fun scriptable() {
		("zero" caseTo expr(_this)).assertEqualsToScript("zero(expr(this()))")
	}
}