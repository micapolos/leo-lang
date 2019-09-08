package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import kotlin.test.Test

class SwitchTest {
	@Test
	fun writer() {
		switchWriter
			.scriptLine(
				switch(
					"zero" caseTo script("foo"),
					"one" caseTo script("bar")))
			.assertEqualTo(
				"switch" lineTo script(
					"case" lineTo script(
						"zero" lineTo script("foo")),
					"case" lineTo script(
						"one" lineTo script("bar"))))
	}

	@Test
	fun caseOrNull() {
		val switch = switch()
			.plus("zero" caseTo script("ten"))
			.plus("one" caseTo script("eleven"))

		switch
			.resolveCaseRhsOrNull(script("bit" lineTo script("zero")))
			.assertEqualTo(script("ten"))

		switch
			.resolveCaseRhsOrNull(script("bit" lineTo script("one")))
			.assertEqualTo(script("eleven"))

		switch
			.resolveCaseRhsOrNull(script("bit" lineTo script("two")))
			.assertEqualTo(null)
	}
}