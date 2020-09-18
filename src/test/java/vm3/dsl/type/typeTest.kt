package vm3.dsl.type

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
}