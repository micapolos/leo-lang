package leo13.js.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedCompilerTest {
	@Test
	fun empty() {
		compile { this }.assertEqualTo(emptyTyped)
	}
}