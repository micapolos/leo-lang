package leo19.typed

import leo.base.assertEqualTo
import leo19.term.function
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable
import leo19.type.case
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.giving
import leo19.type.type
import kotlin.test.Test

class CastTest {
	private val inputTerm = term(variable(128))

	@Test
	fun structToStruct() {
		cast(
			inputTerm,
			type(
				"x" fieldTo type("zero"),
				"y" fieldTo type("one")),
			type(
				"x" fieldTo type("zero"),
				"y" fieldTo type("one")))
			.assertEqualTo(inputTerm)
	}

	@Test
	fun fieldToChoice_simple0() {
		cast(
			inputTerm,
			type("zero" fieldTo type()),
			choice("zero" caseTo type(), "one" caseTo type()))
			.assertEqualTo(term(0))
	}

	@Test
	fun fieldToChoice_simple1() {
		cast(
			inputTerm,
			type("one" fieldTo type()),
			choice("zero" caseTo type(), "one" caseTo type()))
			.assertEqualTo(term(1))
	}

	@Test
	fun fieldToChoice_complex0() {
		cast(
			inputTerm,
			type("zero" fieldTo choice()),
			choice("zero" caseTo choice(), "one" caseTo choice()))
			.assertEqualTo(term(term(0), inputTerm))
	}

	@Test
	fun fieldToChoice_complex1() {
		cast(
			inputTerm,
			type("one" fieldTo choice()),
			choice("zero" caseTo choice(), "one" caseTo choice()))
			.assertEqualTo(term(term(1), inputTerm))
	}

	@Test
	fun arrowToArrow() {
		cast(
			inputTerm,
			type("zero") giving choice("true".case, "false".case),
			choice("zero".case, "one".case) giving choice("true".case, "false".case))
			.assertEqualTo(term(function(inputTerm.invoke(term(0)))))
	}

	@Test
	fun arrowToArrow2() {
		cast(
			inputTerm,
			type("zero") giving choice("true".case, "false".case),
			type("zero") giving type("true"))
			.assertEqualTo(term(function(term(0))))
	}
}