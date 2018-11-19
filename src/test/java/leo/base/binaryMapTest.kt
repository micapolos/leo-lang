package leo.base

import org.junit.Test

class BinaryMapTest {
	@Test
	fun getZero() {
		binaryMap(0, 1).get(Bit.ZERO).assertEqualTo(0)
	}

	@Test
	fun getOne() {
		binaryMap(0, 1).get(Bit.ONE).assertEqualTo(1)
	}

	@Test
	fun setZero() {
		binaryMap(0, 1).set(Bit.ZERO, 2).assertEqualTo(binaryMap(2, 1))
	}

	@Test
	fun setOne() {
		binaryMap(0, 1).set(Bit.ONE, 2).assertEqualTo(binaryMap(0, 2))
	}
}