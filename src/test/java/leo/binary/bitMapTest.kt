package leo.binary

import leo.base.Bit
import leo.base.assertEqualTo
import org.junit.Test

class BitMapTest {
	@Test
	fun empty() {
		emptyBitMap<Int>().assertEqualTo(BitMap(null, null))
	}

	@Test
	fun withBit() {
		bitMap(Bit.ZERO, 0).assertEqualTo(BitMap(0, null))
		bitMap(Bit.ONE, 1).assertEqualTo(BitMap(null, 1))
	}

	@Test
	fun get() {
		BitMap(0, 1).get(Bit.ZERO).assertEqualTo(0)
		BitMap(0, 1).get(Bit.ONE).assertEqualTo(1)
	}

	@Test
	fun set() {
		BitMap(0, 1).set(Bit.ZERO, 2).assertEqualTo(BitMap(2, 1))
		BitMap(0, 1).set(Bit.ONE, 2).assertEqualTo(BitMap(0, 2))
	}

	@Test
	fun plus() {
		BitMap(0, null).plus(Bit.ZERO, 2).assertEqualTo(null)
		BitMap(0, null).plus(Bit.ONE, 2).assertEqualTo(BitMap(0, 2))
		BitMap(null, 1).plus(Bit.ZERO, 2).assertEqualTo(BitMap(2, 1))
		BitMap(null, 1).plus(Bit.ONE, 2).assertEqualTo(null)
	}
}