package leo13.script

import leo.base.assertEqualTo
import leo13.letterName
import kotlin.test.Test
import kotlin.test.assertFails

class LetterTest {
	@Test
	fun char() {
		Letter.A.char.assertEqualTo('a')
	}

	@Test
	fun nameString() {
		Letter.A.nameString.assertEqualTo("a")
	}

	@Test
	fun charLetter() {
		letter('a').assertEqualTo(Letter.A)
		letter('z').assertEqualTo(Letter.Z)
		assertFails { letter('0') }
	}

	@Test
	fun scriptingLine() {
		Letter.A.scriptingLine.assertEqualTo(letterName lineTo script(Letter.A.nameString))
	}
}