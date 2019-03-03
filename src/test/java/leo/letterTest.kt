package leo

import leo.base.assertEqualTo
import leo.base.assertParsedAndRest
import leo.base.onlyStream
import leo.base.then
import leo.binary.Bit
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

	@Test
	fun parse() {
		letterWord.fieldTo(aWord.term)
			.parseLetter
			.assertEqualTo(Letter.A)
	}

	@Test
	fun parseBitStream() {
		Letter.A
			.bitStream
			.then { Bit.ZERO.onlyStream }
			.bitParseLetter
			.assertParsedAndRest(Letter.A, Bit.ZERO.onlyStream)
	}
}