package vm3.dsl.data

import kotlin.test.Test

class DataTest {
	@Test
	fun dsl() {
		10f.data
		struct("x" to 10f.data, "y" to 20f.data)
	}
}
