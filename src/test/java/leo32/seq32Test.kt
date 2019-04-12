package leo32

import leo.base.assertContains
import leo.base.seq
import leo.binary.bit
import leo32.base.i32
import kotlin.test.Test

class Seq32Test {
	@Test
	fun bit32() {
		seq(0.bit, 1.bit, 0.bit, 0.bit, 1.bit)
			.bitSeq32(0b10000000.i32, 0b00010000.i32)
			.assertContains(0b10001001.i32)

		seq(0.bit, 1.bit, 0.bit, 0.bit)
			.bitSeq32(0b10000000.i32, 0b00010000.i32)
			.assertContains()
	}
}