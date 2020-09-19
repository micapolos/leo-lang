package vm3

import leo.base.assertEqualTo
import vm3.dsl.value.array
import vm3.dsl.value.dec
import vm3.dsl.value.get
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class OptimizerTest {
	@Test
	fun optimize() {
		10.value.inc.plus(20.value.dec).optimize.assertEqualTo(30.value)
		array(10.value, 20.value)[0.value.inc].optimize.assertEqualTo(20.value)
		struct("x" to 10.value.inc, "y" to 20.value.inc)["y"].optimize.assertEqualTo(21.value)
	}
}