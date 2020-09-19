package vm3.dsl.value

import leo.base.assertEqualTo
import vm3.code
import kotlin.test.Test

class ValueTest {
	@Test
	fun dsl() {
		false.bool
		10.i32
		10f.f32
		array(10.i32, 20.i32)
		array(10.i32, 20.i32)[0.i32]
		struct("x" to 10.i32, "y" to 20.i32)
		struct("x" to 10.i32, "y" to 20.i32)["x"]
	}

	@Test
	fun code() {
		array(10.i32, 20.i32)[1.i32]
			.code
			.assertEqualTo("[10, 20][1]")

		struct("x" to 10.i32, "y" to 20.i32)["x"]
			.code
			.assertEqualTo("{x: 10, y: 20}.x")
	}
}