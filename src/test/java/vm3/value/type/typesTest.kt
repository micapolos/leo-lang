package vm3.value.type

import leo.base.assertEqualTo
import vm3.dsl.type.f32
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.get
import vm3.dsl.value.give
import vm3.dsl.value.gives
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class TypesTest {
	@Test
	fun get() {
		Types().run {
			get(10f.value).assertEqualTo(f32)

			get(struct("x" to 10f.value, "y" to 20f.value)).assertEqualTo(struct("x" to f32, "y" to f32))
			get(struct("x" to 10f.value, "y" to 20f.value)["x"]).assertEqualTo(f32)
			get(struct("x" to 10f.value, "y" to 20f.value)["y"]).assertEqualTo(f32)

			get(f32.gives(argument).give(10f.value)).assertEqualTo(f32)
			get(f32.gives(argument.plus(argument)).give(10f.value)).assertEqualTo(f32)
			get(f32.gives(10f.value).give(10f.value)).assertEqualTo(f32)
		}
	}
}