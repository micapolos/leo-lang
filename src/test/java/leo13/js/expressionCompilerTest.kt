package leo13.js

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionCompilerTest {
	@Test
	fun empty() {
		compile { this }.assertEqualTo(nullTyped)
	}

	@Test
	fun double() {
		compile { write(token(1.0)) }.assertEqualTo(typed(1.0))
	}

	@Test
	fun call() {
		compile {
			this
				.write(token(begin("native")))
				.write(token("Math"))
				.write(token(end))
				.write(token(begin("call")))
				.write(token("sin"))
				.write(token(begin("with")))
				.write(token(2.0))
				.write(token(end))
				.write(token(end))
		}.assertEqualTo(expression(expression(native("Math")).call("sin", expression(2.0))) of nativeType)
	}
}