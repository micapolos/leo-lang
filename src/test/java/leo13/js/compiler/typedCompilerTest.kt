package leo13.js.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedCompilerTest {
	@Test
	fun empty() {
		compile { this }.assertEqualTo(nullTyped)
	}

	@Test
	fun double() {
		compile { write(token(1.0)) }.assertEqualTo(typed(number(1.0)))
	}
}