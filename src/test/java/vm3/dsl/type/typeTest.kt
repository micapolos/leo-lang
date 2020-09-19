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
	}

	@Test
	fun code() {
		i32[128].code.assertEqualTo("i32[128]")
		struct("x" to f32, "y" to f32).code.assertEqualTo("{x: f32, y: f32}")
	}
}