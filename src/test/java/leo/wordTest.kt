package leo

import leo.base.*
import kotlin.test.Test

class WordTest {
	@Test
	fun string() {
		"foo".wordOrNull?.string.assertEqualTo("foo")
		"".wordOrNull?.string.assertEqualTo(null)
		"int64".wordOrNull?.string.assertEqualTo(null)
		"Foo".wordOrNull?.string.assertEqualTo(null)
		"bąk".wordOrNull?.string.assertEqualTo(null)
		"foo()".wordOrNull?.string.assertEqualTo(null)
	}

	@Test
	fun byteStream() {
		oneWord
			.byteStream
			.assertContains(Letter.O.byte, Letter.N.byte, Letter.E.byte)
	}

	@Test
	fun parse() {
		oneWord
			.bitStream
			.then { EnumBit.ZERO.onlyStream }
			.bitParseWord
			.assertParsedAndRest(oneWord, EnumBit.ZERO.onlyStream)
	}
}