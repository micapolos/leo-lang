package leo14.lambda.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class LibTest {
	@Test
	fun id_() {
		id(123).assertEqualTo(123)
	}

	@Test
	fun which() {
		first(1)(2).assertEqualTo(1)
		second(1)(2).assertEqualTo(2)
	}

	@Test
	fun pair() {
		pair(1)(2)(first).assertEqualTo(1)
		pair(1)(2)(second).assertEqualTo(2)
	}

	@Test
	fun switch() {
		firstOfTwo(1)(fn { it.asInt + 10 })(fn { it.asInt + 20 }).assertEqualTo(11)
		secondOfTwo(2)(fn { it.asInt + 10 })(fn { it.asInt + 20 }).assertEqualTo(22)
	}

	@Test
	fun strings() {
		stringLength("Hello, world!").assertEqualTo(13)
		stringPlusString("Hello, ")("world!").assertEqualTo("Hello, world!")
	}

	@Test
	fun ints() {
		intNegate(1).assertEqualTo(-1)
		intPlusInt(2)(3).assertEqualTo(5)
		intMinusInt(5)(3).assertEqualTo(2)
		intTimesInt(2)(3).assertEqualTo(6)
		intString(2).assertEqualTo("2")
	}

	@Test
	fun lists() {
		listMap(listOf(1, 2, 3))(intString).assertEqualTo(listOf("1", "2", "3"))
	}

	@Test
	fun dot_() {
		5.dot(intMinusInt)(3).assertEqualTo(2)
		5.apply(intMinusInt, 3).assertEqualTo(2)
	}
}