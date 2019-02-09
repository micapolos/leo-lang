package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class BitMapTest {
	@Test
	fun empty() {
		emptyBitMap<Int>().assertEqualTo(BitMap(null, null))
	}

	@Test
	fun withBit() {
		bitMap(Bit0, 0).assertEqualTo(BitMap(0, null))
		bitMap(Bit1, 1).assertEqualTo(BitMap(null, 1))
	}

	@Test
	fun get() {
		BitMap(0, 1).get(Bit0).assertEqualTo(0)
		BitMap(0, 1).get(Bit1).assertEqualTo(1)
	}

	@Test
	fun set() {
		BitMap(0, 1).set(Bit0, 2).assertEqualTo(BitMap(2, 1))
		BitMap(0, 1).set(Bit1, 2).assertEqualTo(BitMap(0, 2))
	}

	@Test
	fun plus() {
		BitMap(0, null).plus(Bit0, 2).assertEqualTo(null)
		BitMap(0, null).plus(Bit1, 2).assertEqualTo(BitMap(0, 2))
		BitMap(null, 1).plus(Bit0, 2).assertEqualTo(BitMap(2, 1))
		BitMap(null, 1).plus(Bit1, 2).assertEqualTo(null)
	}
}