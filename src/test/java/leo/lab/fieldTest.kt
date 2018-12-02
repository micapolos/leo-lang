package leo.lab

import leo.base.assertEqualTo
import leo.oneWord
import leo.twoWord
import kotlin.test.Test

class FieldTest {
	@Test
	fun matchKey() {
		field(oneWord, twoWord.script).matchKey(oneWord) { this }.assertEqualTo(twoWord.script)
		field(oneWord, twoWord.script).matchKey(twoWord) { this }.assertEqualTo(null)
	}
}