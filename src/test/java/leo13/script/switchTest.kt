package leo13.script

import leo13.assertEqualsToScript
import kotlin.test.Test

class SwitchTest {
	@Test
	fun asScriptLine() {
		switch().assertEqualsToScript("switch(null())")

		switch("zero" caseTo expr(), "one" caseTo expr())
			.assertEqualsToScript("switch(case(zero(expr(null())))case(one(expr(null()))))")
	}
}