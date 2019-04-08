package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class TemplateTest {
	@Test
	fun emptyFunction() {
		template()
			.invoke(parameter(term("one")))
			.assertEqualTo(empty.term)
	}

	@Test
	fun applicationFunction() {
		template("one")
			.invoke(parameter(term()))
			.assertEqualTo(term("one"))
	}

	@Test
	fun argumentFunction() {
		template(
			argument,
			"plus" op template("one"))
			.invoke(parameter(term("two")))
			.assertEqualTo(
				term(
					"two" to term(),
					"plus" to term("one")))
	}

	@Test
	fun invokeFunction() {
		val plusOneFunction = template(argument, "plus" op template("one"))
		template(
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
		val function = template(
			argument,
			op(
				switch(
					term("not" to term("false")) gives term("true"),
					term("not" to term("true")) gives term("false"))))

		function.invoke(parameter(term("not" to term("true")))).assertEqualTo(term("false"))
		function.invoke(parameter(term("not" to term("false")))).assertEqualTo(term("true"))
	}
}