package leo16.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class AlignMaskTest {
	@Test
	fun alignmentByteSize() {
		Alignment.BYTE.size.assertEqualTo(1)
		Alignment.SHORT.size.assertEqualTo(2)
		Alignment.INT.size.assertEqualTo(4)
		Alignment.LONG.size.assertEqualTo(8)
	}

	@Test
	fun alignmentIndexMask() {
		Alignment.BYTE.mask.assertEqualTo(0b0)
		Alignment.SHORT.mask.assertEqualTo(0b1)
		Alignment.INT.mask.assertEqualTo(0b11)
		Alignment.LONG.mask.assertEqualTo(0b111)
	}

	@Test
	fun align() {
		Alignment.INT.align(0).assertEqualTo(0)
		Alignment.INT.align(1).assertEqualTo(4)
		Alignment.INT.align(3).assertEqualTo(4)
		Alignment.INT.align(4).assertEqualTo(4)
		Alignment.INT.align(5).assertEqualTo(8)
		Alignment.INT.align(7).assertEqualTo(8)
		Alignment.INT.align(8).assertEqualTo(8)
	}
}