package leo15.lambda.java

import leo.base.assertEqualTo
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
}