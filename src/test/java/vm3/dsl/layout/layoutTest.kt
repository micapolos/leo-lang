package vm3.dsl.layout

import leo.base.assertEqualTo
import kotlin.test.Test

class LayoutTest {
	@Test
	fun dsl() {
		bool.size.assertEqualTo(4)
		i32.size.assertEqualTo(4)
		f32.size.assertEqualTo(4)
		i32[16].size.assertEqualTo(64)
		struct("x" to f32, "y" to f32).size.assertEqualTo(8)
	}
}