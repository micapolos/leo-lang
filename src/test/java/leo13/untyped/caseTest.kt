package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import kotlin.test.Test

class CaseTest {
	@Test
	fun writer() {
		caseWriter
			.scriptLine("zero" caseTo script("foo"))
			.assertEqualTo(
				"case" lineTo script(
					"zero" lineTo script("foo")))
	}

	@Test
	fun reader() {
		caseReader
			.unsafeValue(
				"case" lineTo script(
					"zero" lineTo script("foo")))
			.assertEqualTo("zero" caseTo script("foo"))
	}
}