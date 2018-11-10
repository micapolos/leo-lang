package leo

import leo.Letter
import leo.base.assertEqualTo
import leo.letterOrNull
import kotlin.test.Test

class LetterTest {
	@Test
	fun string() {
		Letter.A.toString().assertEqualTo("letter a")
	}

	@Test
	fun chars() {
		'a'.letterOrNull.assertEqualTo(Letter.A)
		'z'.letterOrNull.assertEqualTo(Letter.Z)
		'A'.letterOrNull.assertEqualTo(null)
		'Z'.letterOrNull.assertEqualTo(null)
		'ą'.letterOrNull.assertEqualTo(null)
		'('.letterOrNull.assertEqualTo(null)
		')'.letterOrNull.assertEqualTo(null)
	}
}