package leo.script

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class ScriptTest {
	@Test
	fun scriptSyntax() {
		nullScript
			.plus(oneWord)
			.plus(plusWord, nullScript.plus(twoWord))
			.plus(timesWord, nullScript.plus(fourWord))
			.string
			.assertEqualTo("one  plus two   times four   ")
	}
}