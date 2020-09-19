package vm3.dsl.type

import leo.base.assertEqualTo
import vm3.code
import kotlin.test.Test

class TypeTest {
	@Test
	fun dsl() {
		bool
		i32
		f32
		i32[128]
		struct("x" to f32, "y" to f32)
		choice("int" to i32, "float" to f32)
	}

	@Test
	fun code() {
		bool.code.assertEqualTo("bool")
		i32.code.assertEqualTo("i32")
		f32.code.assertEqualTo("f32")

		i32[128].code.assertEqualTo("i32[128]")

		struct().code.assertEqualTo("{}")
		struct("x" to i32, "y" to f32).code.assertEqualTo("{ x: i32, y: f32 }")

		choice().code.assertEqualTo("<>")
		choice("int" to i32, "float" to f32).code.assertEqualTo("< int: i32 | float: f32 >")
	}
}