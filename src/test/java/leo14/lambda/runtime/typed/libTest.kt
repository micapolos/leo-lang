package leo14.lambda.runtime.typed

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class LibTest {
	@Test
	fun fn_() {
		fn(int) { typed("ok") }.type.assertEqualTo(int to string)
		assertFails { fn(int) { typed("ok") }(typed("123")) }
		fn(int) { typed("ok") }(typed(123)).value.assertEqualTo("ok")
	}

	@Test
	fun id_() {
		id(int)(typed(123)).value.assertEqualTo(123)
		assertFails { id(int)(typed("123")) }
	}

	@Test
	fun pair_() {
		val pair = pair(string, int)
		val person_t = pair.type
		val person = pair.make
		val personName = pair.first
		val personAge = pair.second
		person.type.assertEqualTo(string to (int to person_t))
		person(typed("Michał"))(typed(44)).type.assertEqualTo(person_t)
		person(typed("Michał"))(typed(44)).dot(personName).type.assertEqualTo(string)
		person(typed("Michał"))(typed(44)).dot(personAge).type.assertEqualTo(int)
		person(typed("Michał"))(typed(44)).dot(personName).value.assertEqualTo("Michał")
		person(typed("Michał"))(typed(44)).dot(personAge).value.assertEqualTo(44)

		assertFails { person(typed(123)) }
		assertFails { person(typed("Michał"))(typed("foo")) }
		assertFails { person(typed("Michał"))(typed(123)).dot(typed(123)) }
	}

	@Test
	fun either_() {
		val either = either("number", int, double)
		either.makeFirst.type.assertEqualTo(int to either.type)
		either.makeSecond.type.assertEqualTo(double to either.type)
		either.makeFirst(typed(123)).type.assertEqualTo(either.type)
		either.makeSecond(typed(123.0)).type.assertEqualTo(either.type)
		assertFails { either.makeFirst(typed(123.0)) }
		assertFails { either.makeSecond(typed(123)) }
//		either.makeFirst(typed(123))
//			.dot(either.switch(string))
//		(typed(234)).type.assertEqualTo(string)
	}

	@Test
	fun firstSecond() {
		first(int, string).type.assertEqualTo(int to (string to int))
		first(int, string)(typed(123)).type.assertEqualTo(string to int)
		first(int, string)(typed(123))(typed("foo")).type.assertEqualTo(int)
		assertFails { first(int, string)(typed("foo")) }
		assertFails { first(int, string)(typed(123))(typed(123)) }

		second(int, string).type.assertEqualTo(int to (string to string))
		second(int, string)(typed(123)).type.assertEqualTo(string to string)
		second(int, string)(typed(123))(typed("foo")).type.assertEqualTo(string)
		assertFails { second(int, string)(typed("foo")) }
		assertFails { second(int, string)(typed(123))(typed(123)) }

		first(int, string)(typed(123))(typed("foo")).value.assertEqualTo(123)
		second(int, string)(typed(123))(typed("foo")).value.assertEqualTo("foo")
	}

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
		assertFails { typedList(int, typed("1")) }
		assertFails { typedList(int, typed(1), typed("2")) }
		assertFails { listMap(int, string)(typedList(string)) }
		assertFails { listMap(int, string)(typedList(int))(intNegate) }

		typedList(int).value
			.assertEqualTo(listOf<Int>())

		typedList(int, typed(1), typed(2), typed(3)).value
			.assertEqualTo(listOf(1, 2, 3))

		listMap(int, string)(typedList(int, typed(1), typed(2), typed(3)))(intString).value
			.assertEqualTo(listOf("1", "2", "3"))
	}

	@Test
	fun longerProgram() {
		typed("Magic number: ")
			.dot(stringPlusString)(
			typed("Hello, ")
				.dot(stringPlusString)(typed("world!"))
				.dot(stringLength)
				.dot(intPlusInt)(typed(10000))
				.dot(intString))
			.value
			.assertEqualTo("Magic number: 10013")
	}
}
