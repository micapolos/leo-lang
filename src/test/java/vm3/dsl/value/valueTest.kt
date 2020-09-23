package vm3.dsl.value

import leo.base.assertEqualTo
import vm3.dsl.type.f32
import vm3.dsl.type.struct
import vm3.value.code
import kotlin.test.Test

class ValueTest {
	@Test
	fun dsl() {
		10f.value

		struct("x" to 10f.value, "y" to 20f.value)
		struct("x" to 10f.value, "y" to 20f.value)["x"]

		argument.switch(
			f32.gives(200f.value),
			struct("f" to f32).gives(struct("f" to 300f.value)))
	}

	@Test
	fun code() {
		struct("x" to 10f.value, "y" to 20f.value)["x"]
			.code
			.assertEqualTo("{x: 10.0, y: 20.0}.x")

		argument
			.switch(
				f32.gives(200f.value),
				struct("x" to f32).gives(struct("x" to 300f.value)))
			.code
			.assertEqualTo("argument.switch { f32 => 200.0, { x: f32 } => {x: 300.0} }")
	}
}