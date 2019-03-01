package leo.script

import leo.base.assertEqualTo
import leo.base.string
import leo.plusWord
import leo.timesWord
import leo.twoWord
import kotlin.test.Test

class ScriptTest {
	@Test
	fun scriptSyntax() {
		script
			.plus(twoWord)
			.plus(plusWord, script.plus(twoWord))
			.plus(timesWord, script.plus(twoWord))
			.string
			.assertEqualTo("two()plus(two())times(two())")
	}
}