package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import kotlin.test.Test

class SwitchTest {
	@Test
	fun reader() {
		switchReader
			.unsafeValue(
				"switch" lineTo script(
					caseWriter.scriptLine("zero" caseTo script("foo")),
					caseWriter.scriptLine("one" caseTo script("bar"))))
			.assertEqualTo(
				switch(
					"zero" caseTo script("foo"),
					"one" caseTo script("bar")))
	}

	@Test
	fun writer() {
		switchWriter
			.scriptLine(
				switch(
					"zero" caseTo script("foo"),
					"one" caseTo script("bar")))
			.assertEqualTo(
				"switch" lineTo script(
					caseWriter.scriptLine("zero" caseTo script("foo")),
					caseWriter.scriptLine("one" caseTo script("bar"))))
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