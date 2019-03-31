package leo32.base

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class Mem32Test {
	@Test
	fun atPut() {
		empty.mem32
			.put(1.i32, 10.i32)
			.put(2.i32, 20.i32)
			.put(3.i32, 30.i32)
			.apply { at(0.i32).assertEqualTo(0.i32) }
			.apply { at(1.i32).assertEqualTo(10.i32) }
			.apply { at(2.i32).assertEqualTo(20.i32) }
			.apply { at(3.i32).assertEqualTo(30.i32) }
			.apply { at(4.i32).assertEqualTo(0.i32) }
	}
}