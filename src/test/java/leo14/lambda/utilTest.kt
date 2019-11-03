package leo14.lambda

import leo.base.assertEqualTo
import leo.base.clampedByte
import leo.base.clampedShort
import leo.binary.bit0
import leo.binary.bit1
import leo13.stack
import leo14.clampedInt2
import leo14.clampedInt4
import kotlin.test.Test

class UtilTest {
	@Test
	fun boolean() {
		term<Any>(false).boolean().assertEqualTo(false)
		term<Any>(true).boolean().assertEqualTo(true)
	}

	@Test
	fun pair() {
		pair<Any>(term(false), term(true)).pair().assertEqualTo(term<Any>(false) to term(true))
	}

	@Test
	fun bit() {
		term<Any>(bit0).bit().assertEqualTo(bit0)
		term<Any>(bit1).bit().assertEqualTo(bit1)
	}

	@Test
	fun ints() {
		term<Any>(1.clampedInt2).int2().assertEqualTo(1.clampedInt2)
		term<Any>(13.clampedInt4).int4().assertEqualTo(13.clampedInt4)
		term<Any>(123.clampedByte).byte().assertEqualTo(123.clampedByte)
		term<Any>(12345.clampedShort).short().assertEqualTo(12345.clampedShort)
		term<Any>(12345678).int().assertEqualTo(12345678)
	}

	@Test
	fun lists() {
		list<Any>().isEmpty.assertEqualTo(true)
		list<Any>().append(term(1)).isEmpty.assertEqualTo(false)
		list<Any>().append(term(1)).link.head.assertEqualTo(term(1))
		list<Any>().append(term(1)).link.tail.assertEqualTo(list())
	}

	@Test
	fun stacks() {
		term(stack<Term<Any>>()).termStack().assertEqualTo(stack())
		term(stack(term<Any>(1), term(2), term(3)))
			.termStack()
			.assertEqualTo(stack(term(1), term(2), term(3)))
	}

	@Test
	fun strings() {
		stringTerm<Any>("").string().assertEqualTo("")
		stringTerm<Any>("foo").string().assertEqualTo("foo")
	}
}