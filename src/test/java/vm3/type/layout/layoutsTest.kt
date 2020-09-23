package vm3.type.layout

import leo.base.assertEqualTo
import vm3.dsl.type.choice
import vm3.dsl.type.f32
import vm3.dsl.type.struct
import kotlin.test.Test

class LayoutsTest {
	@Test
	fun size() {
		Layouts().run {
			get(f32).size.assertEqualTo(4)

			get(struct()).size.assertEqualTo(0)
			get(struct("x" to f32)).size.assertEqualTo(4)
			get(struct("x" to f32, "y" to f32)).size.assertEqualTo(8)

			get(choice()).size.assertEqualTo(4)
			get(choice(f32)).size.assertEqualTo(8)
			get(choice(f32, f32)).size.assertEqualTo(8)
			get(choice(f32, struct("x" to f32, "y" to f32))).size.assertEqualTo(12)
		}
	}
}