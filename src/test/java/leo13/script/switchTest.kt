package leo13.script

import kotlin.test.Test
import kotlin.test.assertFails

class SwitchTest {
	@Test
	fun construction() {
		unsafeSwitch("one" caseTo script("jeden"), "two" caseTo script("dwa"))
		unsafeSwitch("one" caseTo script("jeden"), "two" caseTo script("dwa"), "three" caseTo script("trzy"))
		assertFails { unsafeSwitch("one" caseTo script(), "one" caseTo script()) }
		assertFails { unsafeSwitch("one" caseTo script(), "two" caseTo script(), "one" caseTo script()) }
	}

	@Test
	fun scriptableBody() {
		unsafeSwitch("one" caseTo script("jeden"), "two" caseTo script("dwa"))
			.assertEqualsToScript("one(jeden())two(dwa())")

		unsafeSwitch("one" caseTo script("jeden"), "two" caseTo script("dwa"), "three" caseTo script("trzy"))
			.assertEqualsToScript("one(jeden())two(dwa())three(trzy())")
	}

	@Test
	fun parsing() {
		unsafeSwitch("one" caseTo script("jeden"), "two" caseTo script("dwa"))
			.assertScriptableBodyWorks { unsafeSwitch }

		unsafeSwitch("one" caseTo script("jeden"), "two" caseTo script("dwa"), "three" caseTo script("trzy"))
			.assertScriptableBodyWorks { unsafeSwitch }
	}

//	@Test
//	fun choiceMatchOrNull() {
//		leo13.script.switchOrNull(
//			"zero" caseTo script("zero"),
//			"one" caseTo script("one"))!!
//			.choiceMatchOrNull(
//				unsafeChoice(
//					"one" caseTo type("one"),
//					"zero" caseTo type("zero"))))
//			.assertEqualTo(
//				choiceMatch(
//					match(either("zero", type("zero")), script("zero")),
//					match(either("one", type("one")), script("one"))))
//
//		leo13.script.switchOrNull(
//			"zero" caseTo script("zero"),
//			"one" caseTo script("one"))!!
//			.choiceMatchOrNull(
//				choiceOrNull(
//					either("two", type("two")),
//					either("one", type("one")),
//					either("zero", type("zero")))!!)
//			.assertEqualTo(null)
//	}
}
