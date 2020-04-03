package leo14.lambda.runtime.typed

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class LibTest {
	@Test
	fun ints() {
		intNegate(typed(123))().assertEqualTo(-123)
		intPlusInt(typed(2))(typed(3))().assertEqualTo(5)
		intMinusInt(typed(5))(typed(3))().assertEqualTo(2)
		intTimesInt(typed(2))(typed(3))().assertEqualTo(6)
		intString(typed(123))().assertEqualTo("123")

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
		stringPlusString(typed("Hello, "))(typed("world!"))().assertEqualTo("Hello, world!")
		assertFails { stringPlusString(typed(123)) }
		assertFails { stringPlusString(typed("123"))(typed(123)) }
	}

	@Test
	fun lists() {
		assertFails { typedList(int, typed("1")) }
		assertFails { typedList(int, typed(1), typed("2")) }
		assertFails { listMap(int, string)(typedList(string)) }
		assertFails { listMap(int, string)(typedList(int))(intNegate) }

		typedList(int)()
			.assertEqualTo(listOf<Int>())

		typedList(int, typed(1), typed(2), typed(3))()
			.assertEqualTo(listOf(1, 2, 3))

		listMap(int, string)(typedList(int, typed(1), typed(2), typed(3)))(intString)()
			.assertEqualTo(listOf("1", "2", "3"))
	}
}