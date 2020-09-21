package vm3.type.layout

import leo.base.assertEqualTo
import vm3.dsl.type.bool
import vm3.dsl.type.choice
import vm3.dsl.type.f32
import vm3.dsl.type.get
import vm3.dsl.type.i32
import vm3.dsl.type.struct
import kotlin.test.Test

class LayoutsTest {
	@Test
	fun size() {
		Layouts().run {
			get(bool).size.assertEqualTo(4)
			get(i32).size.assertEqualTo(4)
			get(f32).size.assertEqualTo(4)

			get(i32[0]).size.assertEqualTo(0)
			get(i32[4]).size.assertEqualTo(16)
			get(i32[4][2]).size.assertEqualTo(32)

			get(struct()).size.assertEqualTo(0)
			get(struct("x" to i32)).size.assertEqualTo(4)
			get(struct("x" to i32, "y" to i32)).size.assertEqualTo(8)

			get(choice()).size.assertEqualTo(4)
			get(choice(i32, f32)).size.assertEqualTo(8)
			get(choice(i32, i32[4])).size.assertEqualTo(20)
			get(choice(i32, i32[4], bool[2])).size.assertEqualTo(20)
		}
	}
}