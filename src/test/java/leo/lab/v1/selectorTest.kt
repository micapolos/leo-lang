package leo.lab.v1

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
			.select(leo.lab.v1.selector(rhsGetter, lhsGetter, lhsGetter, rhsGetter, rhsGetter))
			.assertEqualTo(stringWord.script)
	}

	@Test
	fun select_simpleSelectorScript() {
		personScript
			.select(script(oneWord to twoWord.script))
			.assertEqualTo(script(oneWord to twoWord.script))
	}

	@Test
	fun select_complexSelectorScript() {
		personScript
			.select(script(theWord to script(ageWord to script(isWord to metaScript(leo.lab.v1.selector(rhsGetter, rhsGetter))))))
			.assertEqualTo(script(theWord to script(ageWord to script(isWord to numberWord.script))))
	}
}