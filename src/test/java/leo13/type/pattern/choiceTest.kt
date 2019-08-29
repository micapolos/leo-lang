package leo13.type.pattern

import leo.base.assertEqualTo
import leo.base.assertNotNull
import kotlin.test.Test
import kotlin.test.assertFails

class ChoiceTest {
	@Test
	fun constructor() {
		choice("one" caseTo type(), "two" caseTo type()).assertNotNull
		choice("one" caseTo type(), "two" caseTo type(), "three" caseTo type()).assertNotNull

		assertFails { choice("one" caseTo type(), "two" caseTo type(), "one" caseTo type()) }
		assertFails { choice("one" caseTo type(), "two" caseTo type(), "three" caseTo type(), "two" caseTo type()) }
	}

	@Test
	fun rhsOrNull() {
		choice(
			"one" caseTo type("jeden"),
			"two" caseTo type("dwa"),
			"three" caseTo type("trzy"))
			.run {
				rhsOrNull("one").assertEqualTo(type("jeden"))
				rhsOrNull("two").assertEqualTo(type("dwa"))
				rhsOrNull("three").assertEqualTo(type("trzy"))
				rhsOrNull("four").assertEqualTo(null)
			}
	}
}