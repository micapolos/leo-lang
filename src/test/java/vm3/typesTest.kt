package vm3

import leo.base.assertEqualTo
import vm3.dsl.type.bool
import vm3.dsl.type.f32
import vm3.dsl.type.get
import vm3.dsl.type.i32
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.array
import vm3.dsl.value.give
import vm3.dsl.value.get
import vm3.dsl.value.gives
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class TypesTest {
	@Test
	fun get() {
		Types().run {
			get(false.value).assertEqualTo(bool)
			get(10.value).assertEqualTo(i32)
			get(10f.value).assertEqualTo(f32)

			get(struct("x" to 10.value, "y" to 20f.value)).assertEqualTo(struct("x" to i32, "y" to f32))
			get(struct("x" to 10.value, "y" to 20f.value)["x"]).assertEqualTo(i32)
			get(struct("x" to 10.value, "y" to 20f.value)["y"]).assertEqualTo(f32)

			get(array(10.value, 20.value)).assertEqualTo(i32[2])
			get(array(10.value, 20.value)[0.value]).assertEqualTo(i32)
			get(array(10.value, 20.value)[1.value]).assertEqualTo(i32)

			get(i32.gives(argument).give(10.value)).assertEqualTo(i32)
			get(i32.gives(argument.plus(argument)).give(10.value)).assertEqualTo(i32)
			get(i32.gives(10f.value).give(10.value)).assertEqualTo(f32)
		}
	}
}