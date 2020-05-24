package leo16.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class AlignMaskTest {
	@Test
	fun alignmentByteSize() {
		Alignment.ALIGNMENT_1.size.assertEqualTo(1)
		Alignment.ALIGNMENT_2.size.assertEqualTo(2)
		Alignment.ALIGNMENT_4.size.assertEqualTo(4)
		Alignment.ALIGNMENT_8.size.assertEqualTo(8)
	}

	@Test
	fun alignmentIndexMask() {
		Alignment.ALIGNMENT_1.mask.assertEqualTo(0b0)
		Alignment.ALIGNMENT_2.mask.assertEqualTo(0b1)
		Alignment.ALIGNMENT_4.mask.assertEqualTo(0b11)
		Alignment.ALIGNMENT_8.mask.assertEqualTo(0b111)
	}

	@Test
	fun align() {
		Alignment.ALIGNMENT_4.align(0).assertEqualTo(0)
		Alignment.ALIGNMENT_4.align(1).assertEqualTo(4)
		Alignment.ALIGNMENT_4.align(3).assertEqualTo(4)
		Alignment.ALIGNMENT_4.align(4).assertEqualTo(4)
		Alignment.ALIGNMENT_4.align(5).assertEqualTo(8)
		Alignment.ALIGNMENT_4.align(7).assertEqualTo(8)
		Alignment.ALIGNMENT_4.align(8).assertEqualTo(8)
	}
}