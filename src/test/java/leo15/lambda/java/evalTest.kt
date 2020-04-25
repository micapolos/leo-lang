package leo15.lambda.java

import leo.base.assertEqualTo
import leo.base.assertTimesOutMillis
import leo15.lambda.runtime.at
import leo15.lambda.runtime.lambda
import leo15.lambda.runtime.term
import leo15.lambda.runtime.value
import kotlin.test.Test

class EvalTest {
	@Test
	fun prim() {
		term(value(10.java))
			.eval
			.assertEqualTo(10.java)
	}

	@Test
	fun op1() {
		term(
			value(StringLengthJava),
			term(value("Hello, world!".java)))
			.eval
			.assertEqualTo(13.java)
	}

	@Test
	fun op2() {
		term(
			value(StringPlusStringJava),
			term(value("Hello, ".java)),
			term(value("world!".java)))
			.eval
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
			.eval
			.assertEqualTo(6.java)

		term(
			value(IntTimesIntJava),
			term(
				value(IntPlusIntJava),
				term(value(2.java)),
				term(value(2.java))),
			term(value(2.java)))
			.eval
			.assertEqualTo(8.java)
	}

	@Test
	fun printing() {
		term(
			value(PrintingJava("What is it")),
			term(value("foo".java)))
			.eval
			.assertEqualTo("foo".java)
	}

	@Test
	fun id() {
		term(
			lambda(term(at(0))),
			term(value(10.java)))
			.eval
			.assertEqualTo(10.java)
	}

	@Test
	fun loop() {
		assertTimesOutMillis(100) {
			term<Java>(
				lambda(at(0), term(at(0))),
				term(lambda(at(0), term(at(0)))))
				.eval
		}
	}
}