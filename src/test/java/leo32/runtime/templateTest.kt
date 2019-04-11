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
	fun field() {
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
	fun call() {
		val template = template(argument, "plus" op template("one"))
		template(
			argument,
			op(call(template)))
			.invoke(parameter(term("two")))
			.assertEqualTo(
				term(
					"two" to term(),
					"plus" to term("one")))
	}

	@Test
	fun switchFunction() {
		val template = template(
			argument,
			op(
				switch(
					term("not" to term("false")) caseTo term("true"),
					term("not" to term("true")) caseTo term("false"))))

		template.invoke(parameter(term("not" to term("true")))).assertEqualTo(term("false"))
		template.invoke(parameter(term("not" to term("false")))).assertEqualTo(term("true"))
	}
}