package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class BitMapTest {
	@Test
	fun bitToValueMap() {
		bitMap(Bit0, 0).assertEqualTo(BitMap(0, null))
		bitMap(Bit1, 1).assertEqualTo(BitMap(null, 1))
	}

	@Test
	fun get() {
		BitMap<Int>(null, null).get(Bit0).assertEqualTo(null)
		BitMap<Int>(null, null).get(Bit1).assertEqualTo(null)
		BitMap(0, null).get(Bit0).assertEqualTo(0)
		BitMap(0, null).get(Bit1).assertEqualTo(null)
		BitMap(null, 1).get(Bit0).assertEqualTo(null)
		BitMap(null, 1).get(Bit1).assertEqualTo(1)
		BitMap(0, 1).get(Bit0).assertEqualTo(0)
		BitMap(0, 1).get(Bit1).assertEqualTo(1)
	}

	private fun update(int: Int?): Int? =
		when (int) {
			null -> 0
			0 -> 1
			else -> null
		}

	@Test
	fun update() {
		BitMap<Int>(null, null).update(Bit0, this::update).assertEqualTo(BitMap(0, null))
		BitMap<Int>(null, null).update(Bit1, this::update).assertEqualTo(BitMap(null, 0))
		BitMap(0, null).update(Bit0, this::update).assertEqualTo(BitMap(1, null))
		BitMap(0, null).update(Bit1, this::update).assertEqualTo(BitMap(0, 0))
		BitMap(null, 1).update(Bit0, this::update).assertEqualTo(BitMap(0, 1))
		BitMap(null, 1).update(Bit1, this::update).assertEqualTo(BitMap(null, null))
		BitMap(0, 1).update(Bit0, this::update).assertEqualTo(BitMap(1, 1))
		BitMap(0, 1).update(Bit1, this::update).assertEqualTo(BitMap(0, null))
	}

	@Test
	fun plus() {
		BitMap<Int>(null, null).plus(Bit0, 2).assertEqualTo(BitMap(2, null))
		BitMap<Int>(null, null).plus(Bit1, 2).assertEqualTo(BitMap(null, 2))
		BitMap(0, null).plus(Bit0, 2).assertEqualTo(null)
		BitMap(0, null).plus(Bit1, 2).assertEqualTo(BitMap(0, 2))
		BitMap(null, 1).plus(Bit0, 2).assertEqualTo(BitMap(2, 1))
		BitMap(null, 1).plus(Bit1, 2).assertEqualTo(null)
		BitMap(0, 1).plus(Bit0, 2).assertEqualTo(null)
		BitMap(0, 1).plus(Bit1, 2).assertEqualTo(null)
	}
}