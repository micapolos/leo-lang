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
import leo19.type.struct
import kotlin.test.Test

class CastTest {
	private val inputTerm = term(variable(128))

	@Test
	fun structToStruct() {
		cast(
			inputTerm,
			struct(
				"x" fieldTo struct("zero"),
				"y" fieldTo struct("one")),
			struct(
				"x" fieldTo struct("zero"),
				"y" fieldTo struct("one")))
			.assertEqualTo(inputTerm)
	}

	@Test
	fun fieldToChoice_simple0() {
		cast(
			inputTerm,
			struct("zero" fieldTo struct()),
			choice("zero" caseTo struct(), "one" caseTo struct()))
			.assertEqualTo(term(0))
	}

	@Test
	fun fieldToChoice_simple1() {
		cast(
			inputTerm,
			struct("one" fieldTo struct()),
			choice("zero" caseTo struct(), "one" caseTo struct()))
			.assertEqualTo(term(1))
	}

	@Test
	fun fieldToChoice_complex0() {
		cast(
			inputTerm,
			struct("zero" fieldTo choice()),
			choice("zero" caseTo choice(), "one" caseTo choice()))
			.assertEqualTo(term(term(0), inputTerm))
	}

	@Test
	fun fieldToChoice_complex1() {
		cast(
			inputTerm,
			struct("one" fieldTo choice()),
			choice("zero" caseTo choice(), "one" caseTo choice()))
			.assertEqualTo(term(term(1), inputTerm))
	}

	@Test
	fun arrowToArrow() {
		cast(
			inputTerm,
			struct("zero") giving choice("true".case, "false".case),
			choice("zero".case, "one".case) giving choice("true".case, "false".case))
			.assertEqualTo(term(function(inputTerm.invoke(term(0)))))
	}

	@Test
	fun arrowToArrow2() {
		cast(
			inputTerm,
			struct("zero") giving choice("true".case, "false".case),
			struct("zero") giving struct("true"))
			.assertEqualTo(term(function(term(0))))
	}
}