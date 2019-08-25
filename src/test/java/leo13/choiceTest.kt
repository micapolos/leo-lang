package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun asScriptLine() {
		choice()
			.assertEqualsToScriptLine("choice(null())")

		choice(either("zero"), either("one"))
			.assertEqualsToScriptLine("choice(either(zero())either(one()))")

		choice(either("zero", type("foo")), either("one", type("bar")))
			.assertEqualsToScriptLine("choice(either(zero(foo()))either(one(bar())))")
	}

	@Test
	fun choiceOrNull() {
		choice()
			.assertAsScriptLineWorks { choiceOrNull }

		choice(either("zero"), either("one"))
			.assertAsScriptLineWorks { choiceOrNull }

		choice(either("zero", type("foo")), either("one", type("bar")))
			.assertAsScriptLineWorks { choiceOrNull }
	}

	@Test
	fun isValid() {
		choice().isValid.assertEqualTo(true)
		choice(either("zero"), either("one")).isValid.assertEqualTo(true)
		choice(either("zero"), either("zero")).isValid.assertEqualTo(false)
	}
}