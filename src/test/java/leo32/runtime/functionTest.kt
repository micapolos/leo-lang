package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class FunctionTest {
	@Test
	fun emptyFunction() {
		function()
			.invoke(parameter(term("one")))
			.assertEqualTo(empty.term)
	}

	@Test
	fun applicationFunction() {
		function("one")
			.invoke(parameter(term()))
			.assertEqualTo(term("one"))
	}

	@Test
	fun argumentFunction() {
		function(
			argument,
			"plus" op function("one"))
			.invoke(parameter(term("two")))
			.assertEqualTo(
				term(
					"two" to term(),
					"plus" to term("one")))
	}

	@Test
	fun invokeFunction() {
		val plusOneFunction = function(argument, "plus" op function("one"))
		function(
			argument,
			op(call(plusOneFunction)))
			.invoke(parameter(term("two")))
			.assertEqualTo(
				term(
					"two" to term(),
					"plus" to term("one")))
	}

	@Test
	fun switchFunction() {
		val function = function(
			argument,
			op(
				switch(
					term("not" to term("false")) gives term("true"),
					term("not" to term("true")) gives term("false"))))

		function.invoke(parameter(term("not" to term("true")))).assertEqualTo(term("false"))
		function.invoke(parameter(term("not" to term("false")))).assertEqualTo(term("true"))
	}
}