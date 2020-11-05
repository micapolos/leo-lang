package leo21.parser

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.number
import kotlin.test.Test

class ParsersTest {
	@Test
	fun letter() {
		letter.parse("a").assertEqualTo('a')
		letter.parse("z").assertEqualTo('z')
		letter.parse("aa").assertNull
		letter.parse("1").assertNull
	}

	@Test
	fun digit() {
		digit.parse("0").assertEqualTo('0')
		digit.parse("9").assertEqualTo('9')
		digit.parse("99").assertNull
		digit.parse("a").assertNull
	}

	@Test
	fun name() {
		name.parse("").assertNull
		name.parse("a").assertEqualTo("a")
		name.parse("abc").assertEqualTo("abc")
		name.parse("a1").assertNull
	}

	@Test
	fun number() {
		number.parse("").assertNull
		number.parse("1").assertEqualTo(1.number)
		number.parse("123").assertEqualTo(123.number)
		number.parse("123a").assertNull
	}

	@Test
	fun stringLiteral() {
		string.parse("\"foo").assertEqualTo("foo")
	}
}