package leo13.base

import leo.base.assertEqualTo
import kotlin.test.Test

class OptionTest {
	@Test
	fun map() {
		option<Int>().map { toString() }.assertEqualTo(option())
		option(123).map { toString() }.assertEqualTo(option("123"))
	}

	@Test
	fun optionMap() {
		option<Int>().optionMap { option<String>() }.assertEqualTo(option())
		option<Int>().optionMap { option(toString()) }.assertEqualTo(option())
		option(123).optionMap { option<String>() }.assertEqualTo(option())
		option(123).optionMap { option(toString()) }.assertEqualTo(option("123"))
	}
}