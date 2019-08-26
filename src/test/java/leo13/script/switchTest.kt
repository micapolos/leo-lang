package leo13.script

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo.base.assertNull
import leo13.type.*
import kotlin.test.Test

class SwitchTest {
	@Test
	fun construction() {
		leo13.script.switchOrNull().assertNotNull
		leo13.script.switchOrNull("zero" caseTo script(), "one" caseTo script()).assertNotNull
		leo13.script.switchOrNull("zero" caseTo script(), "one" caseTo script(), "zero" caseTo script()).assertNull
	}

	@Test
	fun parsing() {
		"switch()"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(null)

		"switch(null())"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(leo13.script.switchOrNull())

		"switch(case(one(jeden()))case(two(dwa())))"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(
				leo13.script.switchOrNull(
					"one" caseTo script("jeden"),
					"two" caseTo script("dwa")))
	}

	@Test
	fun choiceMatchOrNull() {
		leo13.script.switchOrNull()!!
			.choiceMatchOrNull(choiceOrNull()!!)
			.assertEqualTo(choiceMatch())

		leo13.script.switchOrNull(
			"zero" caseTo script("zero"),
			"one" caseTo script("one"))!!
			.choiceMatchOrNull(
				choiceOrNull(
					either("one", type("one")),
					either("zero", type("zero")))!!)
			.assertEqualTo(
				choiceMatch(
					match(either("zero", type("zero")), script("zero")),
					match(either("one", type("one")), script("one"))))

		leo13.script.switchOrNull(
			"zero" caseTo script("zero"),
			"one" caseTo script("one"))!!
			.choiceMatchOrNull(
				choiceOrNull(
					either("two", type("two")),
					either("one", type("one")),
					either("zero", type("zero")))!!)
			.assertEqualTo(null)
	}
}
