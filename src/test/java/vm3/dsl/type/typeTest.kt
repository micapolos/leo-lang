package vm3.dsl.type

import leo.base.assertEqualTo
import vm3.type.code
import kotlin.test.Test

class TypeTest {
	@Test
	fun dsl() {
		f32
		struct("x" to f32, "y" to f32)
	}

	@Test
	fun code() {
		f32.code.assertEqualTo("f32")

		struct().code.assertEqualTo("{}")
		struct("x" to f32, "y" to f32).code.assertEqualTo("{ x: f32, y: f32 }")

		choice().code.assertEqualTo("<>")
		choice(f32, f32).code.assertEqualTo("< f32 | f32 >")
	}
}