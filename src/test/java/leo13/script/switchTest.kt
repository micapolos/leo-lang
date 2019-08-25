package leo13.script

import leo13.scriptableAssert
import kotlin.test.Test

class SwitchTest {
	@Test
	fun asScriptLine() {
		switch().scriptableAssert("switch(null())")

		switch("zero" caseTo expr(), "one" caseTo expr())
			.scriptableAssert("switch(case(zero(expr(null())))case(one(expr(null()))))")
	}
}