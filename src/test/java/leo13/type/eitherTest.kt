package leo13.type

import leo13.assertAsScriptLineWorks
import leo13.assertEqualsToScriptLine
import kotlin.test.Test

class EitherTest {
	@Test
	fun asScriptLine() {
		either("foo")
			.assertEqualsToScriptLine("either(foo())")

		either("foo", type("bar"))
			.assertEqualsToScriptLine("either(foo(bar()))")
	}

	@Test
	fun eitherOrNull() {
		either("foo")
			.assertAsScriptLineWorks { eitherOrNull }

		either("foo", type("bar"))
			.assertAsScriptLineWorks { eitherOrNull }
	}
}