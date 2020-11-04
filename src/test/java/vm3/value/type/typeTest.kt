package vm3.value.type

import leo.base.assertEqualTo
import vm3.dsl.type.f32
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.give
import vm3.dsl.value.gives
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class TypeTest {
	@Test
	fun type() {
		10f.value.type.assertEqualTo(f32)

		struct("x" to 10f.value, "y" to struct("z" to 10f.value))
			.type
			.assertEqualTo(struct("x" to f32, "y" to struct("z" to f32)))

		10f.value.plus(20f.value).type.assertEqualTo(f32)

		f32.gives(argument).give(10f.value).type.assertEqualTo(f32)
//		f32.gives(struct("x" to argument)).give(10f.value).type.assertEqualTo(struct("x" to f32))
	}
}