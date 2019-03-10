package leo.binary

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class MapTest {
	@Test
	fun putAndGet() {
		nullMap32<Int>()
			.apply { at(154).assertEqualTo(null) }
			.put(154, 15400)
			.apply { at(153).assertEqualTo(null) }
			.apply { at(154).assertEqualTo(15400) }
			.apply { at(155).assertEqualTo(null) }
	}

	@Test
	fun performance() {
		val size = 1 shl 20
		var array32 = nullMap32<Int>()
		for (i in 0 until size) array32.at(i).assertEqualTo(null)
		for (i in 0 until size) array32 = array32.put(i, i)
		for (i in 0 until size) array32.at(i).assertEqualTo(i)
	}

	@Test
	fun performanceNative() {
		val size = 1 shl 20
		val array = arrayOfNulls<Int>(size)
		for (i in 0 until size) array[i].assertEqualTo(null)
		for (i in 0 until size) array[i] = i
		for (i in 0 until size) array[i].assertEqualTo(i)
	}

	@Test
	fun string() {
		nullMap32<Int>()
			.put(5, 50)
			.put(156, 1560)
			.put(123456789, 1234567890)
			.string
			.assertEqualTo("nullMap32().put(5, 50).put(156, 1560).put(123456789, 1234567890)")
	}
}
