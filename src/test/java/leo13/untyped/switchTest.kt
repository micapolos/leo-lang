package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class SwitchTest {
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