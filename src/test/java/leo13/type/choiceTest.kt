package leo13.type

import leo.base.assertNotNull
import leo.base.assertNull
import leo13.assertAsScriptLineWorks
import leo13.assertEqualsToScriptLine
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun construction() {
		choiceOrNull().assertNotNull
		leo13.type.choiceOrNull(either("zero"), either("one")).assertNotNull
		leo13.type.choiceOrNull(either("zero"), either("one"), either("zero")).assertNull
	}

	@Test
	fun asScriptLine() {
		unsafeChoice()
			.assertEqualsToScriptLine("choice(null())")

		unsafeChoice(either("zero"), either("one"))
			.assertEqualsToScriptLine("choice(either(zero())either(one()))")

		unsafeChoice(either("zero", type("foo")), either("one", type("bar")))
			.assertEqualsToScriptLine("choice(either(zero(foo()))either(one(bar())))")
	}

	@Test
	fun choiceOrNull() {
		unsafeChoice()
			.assertAsScriptLineWorks { choiceOrNull }

		unsafeChoice(either("zero"), either("one"))
			.assertAsScriptLineWorks { choiceOrNull }

		unsafeChoice(either("zero", type("foo")), either("one", type("bar")))
			.assertAsScriptLineWorks { choiceOrNull }
	}
}