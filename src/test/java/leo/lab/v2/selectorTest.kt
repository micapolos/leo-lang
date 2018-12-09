package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class SelectorTest {
	private val personScript =
		script(
			personWord to script(
				firstWord to script(nameWord to stringWord.script),
				lastWord to script(nameWord to stringWord.script),
				ageWord to script(numberWord)))

	@Test
	fun select_selector() {
		personScript
			.select(selector(rhsGetter, lhsGetter, lhsGetter, rhsGetter, rhsGetter))
			.assertEqualTo(stringWord.script)
	}
}