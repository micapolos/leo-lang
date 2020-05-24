package leo16.compiler

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class AlignMaskTest {
	@Test
	fun sizeAlignment() {
		0L.sizeAlignmentOrNull.assertNull
		1L.sizeAlignmentOrNull.assertNull
		2L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_1)
		3L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_2)
		4L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_2)
		256L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_8)
		257L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_16)
		65536L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_16)
		65537L.sizeAlignmentOrNull.assertEqualTo(Alignment.ALIGNMENT_32)
	}

	@Test
	fun alignmentBitCount() {
		Alignment.ALIGNMENT_1.bitCount.assertEqualTo(0)
		Alignment.ALIGNMENT_2.bitCount.assertEqualTo(1)
		Alignment.ALIGNMENT_4.bitCount.assertEqualTo(2)
		Alignment.ALIGNMENT_8.bitCount.assertEqualTo(3)
		Alignment.ALIGNMENT_64.bitCount.assertEqualTo(6)
	}

	@Test
	fun alignmentIndexMask() {
		Alignment.ALIGNMENT_1.indexMask.assertEqualTo(0b0)
		Alignment.ALIGNMENT_2.indexMask.assertEqualTo(0b1)
		Alignment.ALIGNMENT_4.indexMask.assertEqualTo(0b11)
		Alignment.ALIGNMENT_8.indexMask.assertEqualTo(0b111)
		Alignment.ALIGNMENT_64.indexMask.assertEqualTo(0b111111)
	}
}