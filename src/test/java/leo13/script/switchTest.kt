package leo13.script

import leo13.assertEqualsToScript
import kotlin.test.Test

class SwitchTest {
	@Test
	fun asScriptLine() {
		switch().assertEqualsToScript("null()")

		switch("zero" caseTo expr(), "one" caseTo expr())
			.assertEqualsToScript("case(zero(expr(null())))case(one(expr(null())))")
	}
}