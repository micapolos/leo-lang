package leo.script

import leo.base.assertEqualTo
import leo.base.string
import leo.plusWord
import leo.timesWord
import kotlin.test.Test

class ScriptTest {
	@Test
	fun scriptSyntax() {
		nullScript
			.plus(2)
			.plus(plusWord, nullScript.plus(2.0f))
			.plus(timesWord, nullScript.plus("cztery"))
			.string
			.assertEqualTo("2, plus(2.0), times(\"cztery\")")
	}
}