package leo13.type

import leo13.script.assertEqualsToScriptLine
import leo13.script.assertScriptableLineWorks
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
			.assertScriptableLineWorks { eitherOrNull }

		either("foo", type("bar"))
			.assertScriptableLineWorks { eitherOrNull }
	}
}