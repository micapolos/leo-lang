package leo.term

import leo.base.assertEqualTo
import leo.stringWord
import kotlin.test.Test

class SelectorTest {
	@Test
	fun select_selector() {
		personScript
			.select(selector(rhsGetter, lhsGetter, lhsGetter, rhsGetter, rhsGetter))
			.assertEqualTo(stringWord.script)
	}
}