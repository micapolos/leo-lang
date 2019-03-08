package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun putAndGet() {
		zeroBitArray32
			.apply { at(154).assertEqualTo(zeroBit) }
			.put(154, oneBit)
			.apply { at(153).assertEqualTo(zeroBit) }
			.apply { at(154).assertEqualTo(oneBit) }
			.apply { at(155).assertEqualTo(zeroBit) }
	}

	@Test
	fun performance() {
		val size = 1 shl 16
		var bitArray32 = zeroBitArray32
		for (i in 0 until size) bitArray32.at(i).assertEqualTo(zeroBit)
		for (i in 0 until size) bitArray32 = bitArray32.put(i, i.clampedBit)
		for (i in 0 until size) bitArray32.at(i).assertEqualTo(i.clampedBit)
	}
}
