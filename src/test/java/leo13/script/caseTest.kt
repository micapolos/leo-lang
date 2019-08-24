package leo13.script

import leo.base.assertEqualTo
import leo13.lineTo
import leo13.script
import kotlin.test.Test

class CaseTest {
	private val evalOrNullCase =
		"one" caseTo expr(op("jeden" lineTo expr()))

	@Test
	fun evalOrNull_match() {
		evalOrNullCase
			.evalOrNull(bindings(), "one" lineTo script("rhs" lineTo script()))
			.assertEqualTo(script("rhs" lineTo script(), "jeden" lineTo script()))
	}

	@Test
	fun evalOrNull_mismatch() {
		evalOrNullCase
			.evalOrNull(bindings(), "two" lineTo script("rhs" lineTo script()))
			.assertEqualTo(null)
	}
}