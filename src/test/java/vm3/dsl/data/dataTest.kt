package vm3.dsl.data

import kotlin.test.Test

class DataTest {
	@Test
	fun dsl() {
		false.data
		10.data
		10f.data
		array(10.data, 20.data)
		struct("x" to 10.data, "y" to 20.data)
	}
}
