package leo13

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo.base.assertNull
import kotlin.test.Test

class SwitchTest {
	@Test
	fun construction() {
		switchOrNull().assertNotNull
		switchOrNull("zero" caseTo script(), "one" caseTo script()).assertNotNull
		switchOrNull("zero" caseTo script(), "one" caseTo script(), "zero" caseTo script()).assertNull
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
			.assertEqualTo(switchOrNull())

		"switch(case(one(jeden()))case(two(dwa())))"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(
				switchOrNull(
					"one" caseTo script("jeden"),
					"two" caseTo script("dwa")))
	}
}
