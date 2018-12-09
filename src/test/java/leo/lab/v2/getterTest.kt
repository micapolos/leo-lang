package leo.lab.v2

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
			oneWord to twoWord.script,
			threeWord to fourWord.script)
			.get(lhsGetter)
			.assertEqualTo(script(oneWord to twoWord.script))

		script(
			oneWord to twoWord.script,
			threeWord to fourWord.script)
			.get(rhsGetter)
			.assertEqualTo(fourWord.script)
	}
}