package leo.lab

import leo.base.assertEqualTo
import leo.oneWord
import leo.threeWord
import leo.twoWord
import org.junit.Test

class OneOfTest {
	@Test
	fun matches() {
		oneWord.script
			.matches(oneOf(oneWord.script(), twoWord.script()), null)
			.assertEqualTo(true)

		twoWord.script
			.matches(oneOf(oneWord.script(), twoWord.script()), null)
			.assertEqualTo(true)

		threeWord.script
			.matches(oneOf(oneWord.script(), twoWord.script()), null)
			.assertEqualTo(false)
	}
}