package vm3

import leo.base.assertEqualTo
import leo.base.assertNotNull
import vm3.dsl.data
import vm3.dsl.i32
import vm3.dsl.input
import vm3.dsl.intInc
import vm3.dsl.intPlus
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun compile() {
		executor(i32, input).assertNotNull
	}

	@Test
	fun bypass() {
		executor(i32, input).run {
			execute(123.data).assertEqualTo(123.data)
			execute(65536.data).assertEqualTo(65536.data)
		}
	}

	@Test
	fun i32() {
		executor(i32, input.intInc.intPlus(input.intInc.intInc)).run {
			execute(10.data).assertEqualTo(23.data)
		}
	}
}