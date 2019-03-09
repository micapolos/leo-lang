package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun putAndGet() {
		0.array32
			.apply { at(154).assertEqualTo(0) }
			.put(154, 15400)
			.apply { at(153).assertEqualTo(0) }
			.apply { at(154).assertEqualTo(15400) }
			.apply { at(155).assertEqualTo(0) }
	}

	@Test
	fun performance() {
		val size = 1 shl 20
		var array32 = 0.array32
		for (i in 0 until size) array32.at(i).assertEqualTo(0)
		for (i in 0 until size) array32 = array32.put(i, i)
		for (i in 0 until size) array32.at(i).assertEqualTo(i)
	}

	@Test
	fun performanceNative() {
		val size = 1 shl 20
		val array = arrayOfNulls<Bit>(size)
		for (i in 0 until size) array[i].assertEqualTo(null)
		for (i in 0 until size) array[i] = i.clampedBit
		for (i in 0 until size) array[i].assertEqualTo(i.clampedBit)
	}
}
