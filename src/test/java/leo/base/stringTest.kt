package leo.base

import leo32.base.i32
import org.junit.Test

class StringTest {
	@Test
	fun byteStreamOrNull_empty() {
		"".byteStreamOrNull.assertEqualTo(null)
	}

	@Test
	fun byteStreamOrNull_nonEmpty() {
		"ab".byteStreamOrNull!!.assertContains(97, 98)
	}

	@Test
	fun byteStreamOrNull_utf8() {
		"ą".byteStreamOrNull!!.assertContains(-60, -123)
	}

	@Test
	fun codePointSeq() {
		"".codePointSeq.assertContains()
		"foo".codePointSeq.assertContains('f'.toInt(), 'o'.toInt(), 'o'.toInt())
		"bąk".codePointSeq.assertContains('b'.toInt(), 'ą'.toInt(), 'k'.toInt())
		"\uD800\uDC00".codePointSeq.assertContains(65536)
	}

	@Test
	fun codePointSeqString() {
		"".codePointSeq.codePointString.assertEqualTo("")
		"foo".codePointSeq.codePointString.assertEqualTo("foo")
		"bąk".codePointSeq.codePointString.assertEqualTo("bąk")
		"\uD800\uDC00".codePointSeq.codePointString.assertEqualTo("\uD800\uDC00")
	}
}