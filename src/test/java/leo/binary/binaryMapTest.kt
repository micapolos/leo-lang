package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class BinaryMapTest {
	@Test
	fun emptyBinaryMap() {
		emptyBinaryMap<Int>().assertEqualTo(leo.binary.Map(emptyChoice()))
	}

	@Test
	fun bitBinaryMap() {
		map(Bit.ZERO, 0).assertEqualTo(leo.binary.Map(array(Bit.ZERO, MapFullMatch(0))))
		map(Bit.ONE, 1).assertEqualTo(leo.binary.Map(array(Bit.ONE, MapFullMatch(1))))
	}

	@Test
	fun binaryBinaryMap() {
		map(binary(Bit.ZERO, Bit.ZERO, Bit.ONE, Bit.ONE), 0)
			.assertEqualTo(leo.binary.Map(array(Bit.ZERO, MapPartialMatch(map(binary(Bit.ZERO, Bit.ONE, Bit.ONE), 0)))))
	}

	@Test
	fun get() {
		val binaryMap =
			leo.binary.Map(Choice(
				MapFullMatch(0),
				MapPartialMatch(Map(Choice(
					MapFullMatch(1),
					null)))))
		binaryMap.get(binary(Bit.ZERO)).assertEqualTo(0)
		binaryMap.get(binary(Bit.ONE)).assertEqualTo(null)
		binaryMap.get(binary(Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		binaryMap.get(binary(Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		binaryMap.get(binary(Bit.ONE, Bit.ZERO)).assertEqualTo(1)
		binaryMap.get(binary(Bit.ONE, Bit.ONE)).assertEqualTo(null)
	}

	@Test
	fun plus() {
		leo.binary.Map<Int>(Choice(null, null))
			.plus(binary(Bit.ZERO), 0)
			.assertEqualTo(leo.binary.Map(Choice(MapFullMatch(0), null)))

		leo.binary.Map<Int>(Choice(null, null))
			.plus(binary(Bit.ONE), 0)
			.assertEqualTo(leo.binary.Map(Choice(null, MapFullMatch(0))))

		leo.binary.Map(Choice(MapFullMatch(0), null))
			.plus(binary(Bit.ZERO), 1)
			.assertEqualTo(null)

		leo.binary.Map(Choice(MapFullMatch(0), null))
			.plus(binary(Bit.ONE), 1)
			.assertEqualTo(leo.binary.Map(Choice(MapFullMatch(0), MapFullMatch(1))))

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(Bit.ZERO), 1)
			.assertEqualTo(null)

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(Bit.ZERO, Bit.ZERO), 1)
			.assertEqualTo(null)

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(Bit.ZERO, Bit.ONE), 1)
			.assertEqualTo(
				leo.binary.Map(Choice(
					MapPartialMatch(Map(Choice(MapFullMatch(0), MapFullMatch(1)))),
					null)))

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(Bit.ONE), 1)
			.assertEqualTo(
				leo.binary.Map(Choice(
					MapPartialMatch(Map(Choice(MapFullMatch(0), null))),
					MapFullMatch(1))))

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(Bit.ONE, Bit.ONE), 1)
			.assertEqualTo(
				leo.binary.Map(Choice(
					MapPartialMatch(Map(Choice(MapFullMatch(0), null))),
					MapPartialMatch(Map(Choice(null, MapFullMatch(1)))))))
	}
}