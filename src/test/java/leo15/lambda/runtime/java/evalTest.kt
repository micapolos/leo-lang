package leo15.lambda.runtime.java

import leo.base.assertEqualTo
import leo.base.assertStackOverflows
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
		assertTimesOutMillis(100) {
			term<Java>(
				lambda(at(0), term(at(0))),
				term(lambda(at(0), term(at(0)))))
				.evalJava
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

	@Test
	fun recursionSmall() {
		term(
			lambda(
				lambda(
					at(1),
					term(at(1)),
					term(at(0)))),
			term(
				lambda(
					lambda(
						value(IntIfZero),
						term(at(0)),
						term(lambda(value(0.java))),
						term(lambda(
							value(IntPlusIntJava),
							term(at(1)),
							term(
								at(2),
								term(at(2)),
								term(
									value(IntDecJava),
									term(at(1))))))))),
			term(value(100.java)))
			.evalJava
			.assertEqualTo(5050.java)
	}

	@Test
	fun recursionHuge() {
		assertStackOverflows {
			term(
				lambda(
					lambda(
						at(1),
						term(at(1)),
						term(at(0)))),
				term(
					lambda(
						lambda(
							value(IntIfZero),
							term(at(0)),
							term(lambda(value(0.java))),
							term(lambda(
								value(IntPlusIntJava),
								term(at(1)),
								term(
									at(2),
									term(at(2)),
									term(
										value(IntDecJava),
										term(at(1))))))))),
				term(value(1000000.java)))
				.evalJava
		}
	}

	@Test
	fun tailRecursionSmall() {
		term(
			lambda(
				lambda(
					at(1),
					term(at(1)),
					term(at(0)))),
			term(
				lambda(
					lambda(
						value(IntIfZero),
						term(at(0)),
						term(lambda(value("OK".java))),
						term(lambda(
							term(
								at(2),
								term(at(2)),
								term(
									value(IntDecJava),
									term(at(1))))))))),
			term(value(100.java)))
			.evalJava
			.assertEqualTo("OK".java)
	}

	@Test
	fun tailRecursionHuge() {
		term(
			lambda(
				lambda(
					at(1),
					term(at(1)),
					term(at(0)))),
			term(
				lambda(
					lambda(
						value(IntIfZero),
						term(at(0)),
						term(lambda(value("OK".java))),
						term(lambda(
							term(
								at(2),
								term(at(2)),
								term(
									value(IntDecJava),
									term(at(1))))))))),
			term(value(1000000.java)))
			.evalJava
			.assertEqualTo("OK".java)
	}

	@Test
	fun tailRecursionHuge_innerTailPosition() {
		term(
			lambda(
				lambda(
					at(1),
					term(at(1)),
					term(at(0)))),
			term(
				lambda(
					lambda(
						value(IntIfZero),
						term(value(IntInvJava), term(at(0))),
						term(lambda(
							term(
								at(2),
								term(at(2)),
								term(
									value(IntDecJava),
									term(at(1)))))),
						term(lambda(value("OK".java)))))),
			term(value(1000000.java)))
			.evalJava
			.assertEqualTo("OK".java)
	}
}