package leo14.lambda.runtime.typed

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class LibTest {
	@Test
	fun ints() {
		intNegate(typed(123)).value.assertEqualTo(-123)
		intPlusInt(typed(2))(typed(3)).value.assertEqualTo(5)
		intMinusInt(typed(5))(typed(3)).value.assertEqualTo(2)
		intTimesInt(typed(2))(typed(3)).value.assertEqualTo(6)
		intString(typed(123)).value.assertEqualTo("123")

		assertFails { intNegate(typed("123")) }
		assertFails { intPlusInt(typed("123")) }
		assertFails { intPlusInt(typed(123))(typed("123")) }
		assertFails { intMinusInt(typed("123")) }
		assertFails { intMinusInt(typed(123))(typed("123")) }
		assertFails { intTimesInt(typed("123")) }
		assertFails { intTimesInt(typed(123))(typed("123")) }
		assertFails { intString.invoke(typed("123")) }
	}

	@Test
	fun strings() {
		assertFails { stringLength(typed(123)) }
		assertFails { stringPlusString(typed(123)) }
		assertFails { stringPlusString(typed("123"))(typed(123)) }

		stringLength(typed("Hello")).value.assertEqualTo(5)
		stringPlusString(typed("Hello, "))(typed("world!")).value.assertEqualTo("Hello, world!")
	}

	@Test
	fun lists() {
		assertFails { typedList(I32, typed("1")) }
		assertFails { typedList(I32, typed(1), typed("2")) }
		assertFails { listMap(I32, Text)(typedList(Text)) }
		assertFails { listMap(I32, Text)(typedList(I32))(intNegate) }

		typedList(I32).value
			.assertEqualTo(listOf<Int>())

		typedList(I32, typed(1), typed(2), typed(3)).value
			.assertEqualTo(listOf(1, 2, 3))

		listMap(I32, Text)(typedList(I32, typed(1), typed(2), typed(3)))(intString).value
			.assertEqualTo(listOf("1", "2", "3"))
	}

	@Test
	fun longerProgram() {
		typed("Magic number: ")
			.apply(
				stringPlusString,
				typed("Hello, ")
					.apply(stringPlusString, typed("world!"))
					.apply(stringLength)
					.apply(intPlusInt, typed(10000))
					.apply(intString))
			.value
			.assertEqualTo("Magic number: 10013")
	}
}
