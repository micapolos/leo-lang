package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class SwitchTest {
	@Test
	fun parsing() {
		"switch()"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(null)

		"switch(null())"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(switch())

		"switch(case(one(jeden()))case(two(dwa())))"
			.unsafeScriptLine
			.switchOrNull
			.assertEqualTo(
				switch(
					"one" caseTo script("jeden"),
					"two" caseTo script("dwa")))
	}
}
