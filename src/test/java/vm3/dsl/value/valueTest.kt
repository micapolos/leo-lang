package vm3.dsl.value

import leo.base.assertEqualTo
import vm3.code
import kotlin.test.Test

class ValueTest {
	@Test
	fun dsl() {
		false.value
		10.value
		10f.value
		array(10.value, 20.value)
		array(10.value, 20.value)[0.value]
		struct("x" to 10.value, "y" to 20.value)
		struct("x" to 10.value, "y" to 20.value)["x"]
	}

	@Test
	fun code() {
		array(10.value, 20.value)[1.value]
			.code
			.assertEqualTo("[10, 20][1]")

		struct("x" to 10.value, "y" to 20.value)["x"]
			.code
			.assertEqualTo("{x: 10, y: 20}.x")
	}
}