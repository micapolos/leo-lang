package leo13.base

import leo.base.assertEqualTo
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class OptionTest {
	@Test
	fun string() {
		option(zeroType, zero).toString().assertEqualTo("zero: option: zero")
	}

	@Test
	fun map() {
		option(zeroType).map(oneType) { one }.assertEqualTo(option(oneType))
		option(zeroType, zero).map(oneType) { one }.assertEqualTo(option(oneType, one))
	}

	@Test
	fun optionMap() {
		option(zeroType).optionMap(oneType) { option(oneType) }.assertEqualTo(option(oneType))
		option(zeroType).optionMap(oneType) { option(oneType, one) }.assertEqualTo(option(oneType))
		option(zeroType, zero).optionMap(oneType) { option(oneType) }.assertEqualTo(option(oneType))
		option(zeroType, zero).optionMap(oneType) { option(oneType, one) }.assertEqualTo(option(oneType, one))
	}
}