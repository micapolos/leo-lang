package leo13.script

import leo13.scriptableAssert
import kotlin.test.Test

class CaseTest {
	@Test
	fun scriptable() {
		("zero" caseTo expr()).scriptableAssert("case(zero()to(expr(null())))")
	}
}