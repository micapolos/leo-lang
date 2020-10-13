package leo19.decompiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.term.nullTerm
import leo19.term.term
import leo19.type.case
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.struct
import leo19.typed.of
import org.junit.Test

class DecompilerTest {
	@Test
	fun choice_simple() {
		term(0)
			.of(choice("zero".case, "one".case))
			.script
			.assertEqualTo(script("zero"))

		term(1)
			.of(choice("zero".case, "one".case))
			.script
			.assertEqualTo(script("one"))
	}

	@Test
	fun choice_complex() {
		term(term(0), term(1))
			.of(
				choice(
					"bit" caseTo choice("zero".case, "one".case),
					"boolean" caseTo choice("true".case, "false".case)))
			.script
			.assertEqualTo(script("bit" lineTo script("one")))
	}

	@Test
	fun struct_static() {
		nullTerm
			.of(
				struct(
					"x" fieldTo struct(),
					"y" fieldTo struct()))
			.script
			.assertEqualTo(
				script(
					"x" lineTo script(),
					"y" lineTo script()))
	}

	@Test
	fun struct_simple() {
		term(0)
			.of(
				struct(
					"x" fieldTo struct(),
					"y" fieldTo choice("zero".case, "one".case)))
			.script
			.assertEqualTo(
				script(
					"x" lineTo script(),
					"y" lineTo script("zero")))
	}

	@Test
	fun struct_complex() {
		term(term(0), term(1))
			.of(
				struct(
					"x" fieldTo choice("zero".case, "one".case),
					"y" fieldTo choice("zero".case, "one".case)))
			.script
			.assertEqualTo(
				script(
					"x" lineTo script("zero"),
					"y" lineTo script("one")))
	}
}
