package leo13.value

import leo13._this
import leo13.script.assertEqualsToScript
import kotlin.test.Test

class SwitchTest {
	@Test
	fun asScriptLine() {
		switch().assertEqualsToScript("null()")

		switch("zero" caseTo expr(_this), "one" caseTo expr(_this))
			.assertEqualsToScript("case(zero(expr(this())))case(one(expr(this())))")
	}
}