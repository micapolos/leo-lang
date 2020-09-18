package vm3.dsl.data

import kotlin.test.Test

class DataTest {
	@Test
	fun dsl() {
		false.bool
		10.i32
		10f.f32
		array(10.i32, 20.i32)
		struct("x" to 10.i32, "y" to 20.i32)
	}
}
