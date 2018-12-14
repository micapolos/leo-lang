package leo.term

import leo.base.assertEqualTo
import leo.fourWord
import leo.oneWord
import leo.threeWord
import leo.twoWord
import org.junit.Test

class GetterTest {
	@Test
	fun get() {
		script(
			oneWord apply twoWord.script,
			threeWord apply fourWord.script)
			.get(lhsGetter)
			.assertEqualTo(script(oneWord apply twoWord.script))

		script(
			oneWord apply twoWord.script,
			threeWord apply fourWord.script)
			.get(rhsGetter)
			.assertEqualTo(fourWord.script)
	}
}