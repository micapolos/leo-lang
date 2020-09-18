package vm3

import leo.base.assertEqualTo
import leo.base.assertNotNull
import vm3.dsl.data
import vm3.dsl.fn
import vm3.dsl.i32
import vm3.dsl.inc
import vm3.dsl.plus
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun compile() {
		i32.fn { this }.executor.assertNotNull
	}

	@Test
	fun bypass() {
		i32.fn { this }.executor.run {
			execute(123.data).assertEqualTo(123.data)
			execute(65536.data).assertEqualTo(65536.data)
		}
	}

	@Test
	fun i32() {
		i32.fn { inc + inc.inc }.executor.run {
			execute(10.data).assertEqualTo(23.data)
		}
	}
}