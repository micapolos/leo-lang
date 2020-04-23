package leo15.lambda.java

import leo.base.assertEqualTo
import leo15.lambda.runtime.application
import leo15.lambda.runtime.atom
import leo15.lambda.runtime.term
import kotlin.test.Test

class EvalTest {
	@Test
	fun prim() {
		term(atom(10.java), null)
			.eval
			.assertEqualTo(10.java)
	}

	@Test
	fun op1() {
		term(atom(LengthJava),
			application(term(atom("Hello, world!".java), null),
				null))
			.eval
			.assertEqualTo(13.java)
	}

	@Test
	fun op2() {
		term(atom(PlusJava),
			application(term(atom("Hello, ".java), null),
				application(term(atom("world!".java), null),
					null)))
			.eval
			.assertEqualTo("Hello, world!".java)
	}
}