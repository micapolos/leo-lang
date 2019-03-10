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
		map(zero.bit, 0).assertEqualTo(leo.binary.Map(array(zero.bit, MapFullMatch(0))))
		map(one.bit, 1).assertEqualTo(leo.binary.Map(array(one.bit, MapFullMatch(1))))
	}

	@Test
	fun binaryBinaryMap() {
		map(binary(zero.bit, zero.bit, one.bit, one.bit), 0)
			.assertEqualTo(leo.binary.Map(array(zero.bit, MapPartialMatch(map(binary(zero.bit, one.bit, one.bit), 0)))))
	}

	@Test
	fun get() {
		val binaryMap =
			leo.binary.Map(Choice(
				MapFullMatch(0),
				MapPartialMatch(Map(Choice(
					MapFullMatch(1),
					null)))))
		binaryMap.get(binary(zero.bit)).assertEqualTo(0)
		binaryMap.get(binary(one.bit)).assertEqualTo(null)
		binaryMap.get(binary(zero.bit, zero.bit)).assertEqualTo(null)
		binaryMap.get(binary(zero.bit, one.bit)).assertEqualTo(null)
		binaryMap.get(binary(one.bit, zero.bit)).assertEqualTo(1)
		binaryMap.get(binary(one.bit, one.bit)).assertEqualTo(null)
	}

	@Test
	fun plus() {
		leo.binary.Map<Int>(Choice(null, null))
			.plus(binary(zero.bit), 0)
			.assertEqualTo(leo.binary.Map(Choice(MapFullMatch(0), null)))

		leo.binary.Map<Int>(Choice(null, null))
			.plus(binary(one.bit), 0)
			.assertEqualTo(leo.binary.Map(Choice(null, MapFullMatch(0))))

		leo.binary.Map(Choice(MapFullMatch(0), null))
			.plus(binary(zero.bit), 1)
			.assertEqualTo(null)

		leo.binary.Map(Choice(MapFullMatch(0), null))
			.plus(binary(one.bit), 1)
			.assertEqualTo(leo.binary.Map(Choice(MapFullMatch(0), MapFullMatch(1))))

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(zero.bit), 1)
			.assertEqualTo(null)

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(zero.bit, zero.bit), 1)
			.assertEqualTo(null)

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(zero.bit, one.bit), 1)
			.assertEqualTo(
				leo.binary.Map(Choice(
					MapPartialMatch(Map(Choice(MapFullMatch(0), MapFullMatch(1)))),
					null)))

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(one.bit), 1)
			.assertEqualTo(
				leo.binary.Map(Choice(
					MapPartialMatch(Map(Choice(MapFullMatch(0), null))),
					MapFullMatch(1))))

		leo.binary.Map(Choice(MapPartialMatch(Map(Choice(MapFullMatch(0), null))), null))
			.plus(binary(one.bit, one.bit), 1)
			.assertEqualTo(
				leo.binary.Map(Choice(
					MapPartialMatch(Map(Choice(MapFullMatch(0), null))),
					MapPartialMatch(Map(Choice(null, MapFullMatch(1)))))))
	}
}