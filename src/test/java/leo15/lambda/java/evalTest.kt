package leo15.lambda.java

import leo.base.assertEqualTo
import leo.base.assertTimesOutMillis
import leo15.lambda.runtime.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EvalTest {
	@Test
	fun prim() {
		term(value(10.java))
			.evalJava
			.assertEqualTo(10.java)
	}

	@Test
	fun op1() {
		term(
			value(StringLengthJava),
			term(value("Hello, world!".java)))
			.evalJava
			.assertEqualTo(13.java)
	}

	@Test
	fun op2() {
		term(
			value(StringPlusStringJava),
			term(value("Hello, ".java)),
			term(value("world!".java)))
			.evalJava
			.assertEqualTo("Hello, world!".java)
	}

	@Test
	fun op3() {
		term(
			value(IntPlusIntJava),
			term(value(2.java)),
			term(
				value(IntTimesIntJava),
				term(value(2.java)),
				term(value(2.java))))
			.evalJava
			.assertEqualTo(6.java)

		term(
			value(IntTimesIntJava),
			term(
				value(IntPlusIntJava),
				term(value(2.java)),
				term(value(2.java))),
			term(value(2.java)))
			.evalJava
			.assertEqualTo(8.java)
	}

	@Test
	fun printing() {
		term(
			value(PrintingJava("What is it")),
			term(value("foo".java)))
			.evalJava
			.assertEqualTo("foo".java)
	}

	@Test
	fun id() {
		term(
			lambda(term(at(0))),
			term(value(10.java)))
			.evalJava
			.assertEqualTo(10.java)
	}

	@Test
	fun first() {
		term(
			lambda(lambda(at(1))),
			term(value(10.java)),
			term(value(20.java)))
			.evalJava
			.assertEqualTo(10.java)
	}

	@Test
	fun second() {
		term(
			lambda(lambda(at(0))),
			term(value(10.java)),
			term(value(20.java)))
			.evalJava
			.assertEqualTo(20.java)
	}

	@Test
	fun pairFirst() {
		term(
			lambda(lambda(lambda(at(0), term(at(2)), term(at(1))))),
			term(value(10.java)),
			term(value(20.java)),
			term(lambda(lambda(at(1)))))
			.evalJava
			.assertEqualTo(10.java)
	}

	@Test
	fun pairSecond() {
		term(
			lambda(lambda(lambda(at(0), term(at(2)), term(at(1))))),
			term(value(10.java)),
			term(value(20.java)),
			term(lambda(lambda(at(0)))))
			.evalJava
			.assertEqualTo(20.java)
	}

	@Test
	fun eitherFirst() {
		term(
			lambda(lambda(lambda(at(1), term(at(2))))),
			term(value(10.java)),
			term(value(IntMinusIntJava), term(value(100.java))),
			term(value(StringPlusStringJava), term(value("ok: ".java))))
			.evalJava
			.assertEqualTo(90.java)
	}

	@Test
	fun eitherSecond() {
		term(
			lambda(lambda(lambda(at(0), term(at(2))))),
			term(value("foo".java)),
			term(value(IntMinusIntJava), term(value(100.java))),
			term(value(StringPlusStringJava), term(value("ok: ".java))))
			.evalJava
			.assertEqualTo("ok: foo".java)
	}

	@Test
	fun loop() {
		term<Java>(
			lambda(at(0), term(at(0))),
			term(lambda(at(0), term(at(0)))))
			.run {
				if (tailOptimization) {
					assertTimesOutMillis(100) { evalJava }
				} else {
					assertFailsWith(StackOverflowError::class) { evalJava }
				}
			}
	}

	@Test
	fun ifZero_zero() {
		term(
			value(IntIfZero),
			term(value(0.java)),
			term(lambda(value("zero".java))),
			term(lambda(value("not zero".java))))
			.evalJava
			.assertEqualTo("zero".java)
	}

	@Test
	fun ifZero_notZero() {
		term(
			value(IntIfZero),
			term(value(123.java)),
			term(lambda(value("zero".java))),
			term(lambda(value("not zero".java))))
			.evalJava
			.assertEqualTo("not zero".java)
	}
}