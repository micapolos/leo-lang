package vm3

import leo.base.assertEqualTo
import vm3.dsl.value.array
import vm3.dsl.value.dec
import vm3.dsl.value.get
import vm3.dsl.value.i32
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import kotlin.test.Test

class OptimizerTest {
	@Test
	fun optimize() {
		10.i32.inc.plus(20.i32.dec).optimize.assertEqualTo(30.i32)
		array(10.i32, 20.i32)[0.i32.inc].optimize.assertEqualTo(20.i32)
		struct("x" to 10.i32.inc, "y" to 20.i32.inc)["y"].optimize.assertEqualTo(21.i32)
	}
}