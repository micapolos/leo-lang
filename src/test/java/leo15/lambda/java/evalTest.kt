package leo15.lambda.java

import leo.base.assertEqualTo
import leo.base.assertTimesOutMillis
import leo15.lambda.invoke
import leo15.lambda.lambda
import leo15.lambda.resolveVars
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
			value(LengthJava),
			term(value("Hello, world!".java)))
			.eval
			.assertEqualTo(13.java)
	}

	@Test
	fun op2() {
		term(
			value(PlusJava),
			term(value("Hello, ".java)),
			term(value("world!".java)))
			.eval
			.assertEqualTo("Hello, world!".java)
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
				lambda { f ->
					f.invoke(f)
				}.invoke(
					lambda { f ->
						f.invoke(f)
					}).resolveVars)
				.eval
		}
	}
}