package vm3

import leo.base.assertEqualTo
import vm3.dsl.value.get
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class OptimizerTest {
	@Test
	fun optimize() {
		10f.value.plus(20f.value).optimize.assertEqualTo(30f.value)
		struct("x" to 10f.value, "y" to 20f.value.plus(30f.value))["y"].optimize.assertEqualTo(50f.value)
	}
}