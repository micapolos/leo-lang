package leo13.script

import leo.base.assertEqualTo
import leo13.assertFailsWith
import kotlin.test.Test
import kotlin.test.assertFails

class SwitchTest {
	@Test
	fun construction() {
		switch("one" caseTo script("jeden"), "two" caseTo script("dwa"))
		switch("one" caseTo script("jeden"), "two" caseTo script("dwa"), "three" caseTo script("trzy"))
		assertFails { switch("one" caseTo script(), "one" caseTo script()) }
		assertFails { switch("one" caseTo script(), "two" caseTo script(), "one" caseTo script()) }
	}

	@Test
	fun scriptableLine() {
		switch("one" caseTo script("jeden"), "two" caseTo script("dwa"))
			.scriptableLine
			.assertEqualTo(
				"switch" lineTo script(
					"one" lineTo script(),
					"gives" lineTo script("jeden"),
					"two" lineTo script(),
					"gives" lineTo script("dwa")))

		switch("one" caseTo script("jeden"), "two" caseTo script("dwa"), "three" caseTo script("trzy"))
			.scriptableLine
			.assertEqualTo(
				"switch" lineTo script(
					"one" lineTo script(),
					"gives" lineTo script("jeden"),
					"two" lineTo script(),
					"gives" lineTo script("dwa"),
					"three" lineTo script(),
					"gives" lineTo script("trzy")))
	}

	@Test
	fun parsing() {
		assertFailsWith(script("switch")) {
			script().switch
		}

		assertFailsWith(
			script(
				"switch" lineTo script(
					"case" lineTo script("one")))) {
			script("one").switch
		}

		assertFailsWith(
			script(
				"switch" lineTo script(
					"case" lineTo script(
						"one" lineTo script("more"))))) {
			script("one" lineTo script("more")).switch
		}

		assertFailsWith(
			script(
				"switch" lineTo script(
					"case" lineTo script(
						"one" lineTo script(),
						"give" lineTo script("two"))))) {
			script(
				"one" lineTo script(),
				"give" lineTo script("two")).switch
		}

		assertFailsWith(
			script(
				"switch" lineTo script(
					"single" lineTo script(
						"case" lineTo script(
							"one" lineTo script(),
							"gives" lineTo script("two")))))) {
			script(
				"one" lineTo script(),
				"gives" lineTo script("two")).switch
		}

		assertFailsWith(
			script(
				"switch" lineTo script(
					"duplicate" lineTo script(
						"case" lineTo script(
							"one" lineTo script(),
							"gives" lineTo script("two")),
						"case" lineTo script(
							"one" lineTo script(),
							"gives" lineTo script("four")))))) {
			script(
				"one" lineTo script(),
				"gives" lineTo script("two"),
				"two" lineTo script(),
				"gives" lineTo script("three"),
				"one" lineTo script(),
				"gives" lineTo script("four")).switch
		}

		switch("one" caseTo script("jeden"), "two" caseTo script("dwa"))
			.assertScriptableBodyWorks { switch }

		switch("one" caseTo script("jeden"), "two" caseTo script("dwa"), "three" caseTo script("trzy"))
			.assertScriptableBodyWorks { switch }
	}
}
