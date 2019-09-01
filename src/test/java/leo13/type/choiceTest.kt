package leo13.type

import leo.base.assertEqualTo
import leo.base.assertNotNull
import kotlin.test.Test
import kotlin.test.assertFails

class ChoiceTest {
	@Test
	fun constructor() {
		unsafeChoice("one" caseTo type(), "two" caseTo type()).assertNotNull
		unsafeChoice("one" caseTo type(), "two" caseTo type(), "three" caseTo type()).assertNotNull

		assertFails { unsafeChoice("one" caseTo type(), "two" caseTo type(), "one" caseTo type()) }
		assertFails { unsafeChoice("one" caseTo type(), "two" caseTo type(), "three" caseTo type(), "two" caseTo type()) }
	}

	@Test
	fun rhsOrNull() {
		unsafeChoice(
			"one" caseTo type("jeden"),
			"two" caseTo type("dwa"),
			"three" caseTo type("trzy"))
			.run {
				rhsOrNull("one").assertEqualTo(rhs(type("jeden")))
				rhsOrNull("two").assertEqualTo(rhs(type("dwa")))
				rhsOrNull("three").assertEqualTo(rhs(type("trzy")))
				rhsOrNull("four").assertEqualTo(null)
			}
	}
}