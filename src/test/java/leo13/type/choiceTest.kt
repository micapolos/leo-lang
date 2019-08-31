package leo13.type

import leo.base.assertEqualTo
import leo.base.assertNotNull
import kotlin.test.Test
import kotlin.test.assertFails

class ChoiceTest {
	@Test
	fun constructor() {
		unsafeChoice("one" caseTo pattern(), "two" caseTo pattern()).assertNotNull
		unsafeChoice("one" caseTo pattern(), "two" caseTo pattern(), "three" caseTo pattern()).assertNotNull

		assertFails { unsafeChoice("one" caseTo pattern(), "two" caseTo pattern(), "one" caseTo pattern()) }
		assertFails { unsafeChoice("one" caseTo pattern(), "two" caseTo pattern(), "three" caseTo pattern(), "two" caseTo pattern()) }
	}

	@Test
	fun rhsOrNull() {
		unsafeChoice(
			"one" caseTo pattern("jeden"),
			"two" caseTo pattern("dwa"),
			"three" caseTo pattern("trzy"))
			.run {
				rhsOrNull("one").assertEqualTo(thunk(pattern("jeden")))
				rhsOrNull("two").assertEqualTo(thunk(pattern("dwa")))
				rhsOrNull("three").assertEqualTo(thunk(pattern("trzy")))
				rhsOrNull("four").assertEqualTo(null)
			}
	}
}