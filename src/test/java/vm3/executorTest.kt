package vm3

import leo.base.assertEqualTo
import leo.base.assertNotNull
import vm3.dsl.data.f32
import vm3.dsl.data.i32
import vm3.dsl.type.f32
import vm3.dsl.type.i32
import vm3.dsl.value.fn
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun compile() {
		i32.fn { this }.executor.assertNotNull
	}

	@Test
	fun bypass() {
		i32.fn { this }.executor.run {
			execute(123.i32).assertEqualTo(123.i32)
			execute(65536.i32).assertEqualTo(65536.i32)
		}
	}

	@Test
	fun i32() {
		i32.fn { inc + inc.inc }.executor.run {
			execute(10.i32).assertEqualTo(23.i32)
		}
	}

	@Test
	fun f32() {
		f32.fn { this + this }.executor.run {
			execute(12.3f.f32).assertEqualTo(24.6f.f32)
		}
	}
}