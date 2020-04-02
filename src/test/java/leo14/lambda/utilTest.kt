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
		empty<Any>().isEmpty.assertEqualTo(true)
		empty<Any>().append(term(1)).isEmpty.assertEqualTo(false)
		empty<Any>().append(term(1)).link.head.assertEqualTo(term(1))
		empty<Any>().append(term(1)).link.tail.assertEqualTo(empty())
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

	@Test
	fun makeTupleTerm() {
		tupleTerm<Any>()
			.assertEqualTo(fn(arg0()))
		tupleTerm(term("first"))
			.assertEqualTo(fn(fn(arg0<String>()(arg1())))(term("first")))
		tupleTerm(term("first"), term("second"))
			.assertEqualTo(fn(fn(fn(arg0<String>()(arg2())(arg1()))))(term("first"))(term("second")))
		tupleTerm(term("first"), term("second"), term("third"))
			.assertEqualTo(fn(fn(fn(fn(arg0<String>()(arg3())(arg2())(arg1())))))(term("first"))(term("second"))(term("third")))
	}

	@Test
	fun choice() {
		choiceTerm(0, 1, term("0 of 1"))
			.assertEqualTo(fn(arg0<Any>().invoke(term("0 of 1"))))

		choiceTerm(0, 2, term("0 of 2"))
			.assertEqualTo(fn(fn(arg0<Any>().invoke(term("0 of 2")))))
		choiceTerm(1, 2, term("1 of 2"))
			.assertEqualTo(fn(fn(arg1<Any>().invoke(term("1 of 2")))))
	}

	@Test
	fun match() {
		term("choice")
			.matchTerm(term("case1"), term("case0"))
			.assertEqualTo(term("choice").invoke(term("case1")).invoke(term("case0")))
	}
}