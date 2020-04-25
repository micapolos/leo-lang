package leo15.lambda.java

import leo.base.assertEqualTo
import leo.base.assertTimesOutMillis
import leo15.lambda.invoke
import leo15.lambda.lambda
import leo15.lambda.resolveVars
import leo15.lambda.runtime.atom
import leo15.lambda.runtime.term
import kotlin.test.Test

class EvalTest {
	@Test
	fun prim() {
		term(atom(10.java))
			.eval
			.assertEqualTo(10.java)
	}

	@Test
	fun op1() {
		term(
			atom(LengthJava),
			term(atom("Hello, world!".java)))
			.eval
			.assertEqualTo(13.java)
	}

	@Test
	fun op2() {
		term(
			atom(PlusJava),
			term(atom("Hello, ".java)),
			term(atom("world!".java)))
			.eval
			.assertEqualTo("Hello, world!".java)
	}

	@Test
	fun printing() {
		term(
			atom(PrintingJava("What is it")),
			term(atom("foo".java)))
			.eval
			.assertEqualTo("foo".java)
	}

	@Test
	fun id() {
		term(
			atom(term(atom(0))),
			term(atom(10.java)))
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