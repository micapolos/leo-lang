package leo13.script

import leo.base.assertEqualTo
import leo13.wordName
import kotlin.test.Test
import kotlin.test.assertFails

class WordTest {
	@Test
	fun stringWord() {
		word("zero").assertEqualTo(word(Letter.Z).plus(Letter.E).plus(Letter.R).plus(Letter.O))
		assertFails { word("123") }
	}

	@Test
	fun name() {
		word("zero")
			.name
			.assertEqualTo("zero")
	}

	@Test
	fun scriptingLine() {
		word("zero")
			.scriptingLine
			.assertEqualTo(wordName lineTo script(word("zero").name))
	}
}