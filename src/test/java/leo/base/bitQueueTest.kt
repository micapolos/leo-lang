package leo.base

import kotlin.test.Test

class BitQueueTest {
	@Test
	fun string() {
		bitQueue(1, 0, 0)
			.string
			.assertEqualTo("<100>")
	}

	@Test
	fun pushHead_growFromNull() {
		nullOf<BitQueue>()
			.orNullPushHead
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(0),
					tailIndexBinary = null,
					headIndexBinary = null))
	}

	@Test
	fun pushHead_growFromSingleton() {
		BitQueue(
			bitArray = bitArray(1),
			tailIndexBinary = null,
			headIndexBinary = null)
			.pushHead
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(1, 1),
					tailIndexBinary = binary(0),
					headIndexBinary = binary(1)))
	}

	@Test
	fun pushHead_noGrow() {
		BitQueue(
			bitArray = bitArray(1, 0, 1, 1),
			tailIndexBinary = binary(0, 1),
			headIndexBinary = binary(1, 0))
			.pushHead
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(1, 0, 1, 1),
					tailIndexBinary = binary(0, 1),
					headIndexBinary = binary(1, 1)))
	}

	@Test
	fun pushHead_grow() {
		BitQueue(
			bitArray = bitArray(1, 0, 1, 1),
			tailIndexBinary = binary(0, 1),
			headIndexBinary = binary(1, 1))
			.pushHead
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(1, 0, 1, 1, 1, 0, 1, 1),
					tailIndexBinary = binary(0, 0, 1),
					headIndexBinary = binary(1, 0, 0)))
	}

	@Test
	fun popTail_noShrink() {
		BitQueue(
			bitArray = bitArray(1, 0, 1, 1, 0, 1, 0, 0),
			tailIndexBinary = binary(0, 1, 0),
			headIndexBinary = binary(1, 0, 1))
			.popTail
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(1, 0, 1, 1, 0, 1, 0, 0),
					tailIndexBinary = binary(0, 1, 1),
					headIndexBinary = binary(1, 0, 1)))
	}

	@Test
	fun popTail_shrink() {
		BitQueue(
			bitArray = bitArray(1, 0, 1, 1, 0, 1, 0, 0),
			tailIndexBinary = binary(0, 1, 1),
			headIndexBinary = binary(1, 0, 1))
			.popTail
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(0, 1, 0, 0),
					tailIndexBinary = binary(0, 0),
					headIndexBinary = binary(0, 1)))
	}

	@Test
	fun popTail_shrinkToSingleton() {
		BitQueue(
			bitArray = bitArray(1, 0),
			tailIndexBinary = binary(0),
			headIndexBinary = binary(1))
			.popTail
			.assertEqualTo(
				BitQueue(
					bitArray = bitArray(0),
					tailIndexBinary = null,
					headIndexBinary = null))
	}

	@Test
	fun popTail_shrinkToNull() {
		BitQueue(
			bitArray = bitArray(1),
			tailIndexBinary = null,
			headIndexBinary = null)
			.popTail
			.assertEqualTo(null)
	}
}