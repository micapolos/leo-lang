package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	val testTerm = term(
		oneWord.field,
		negateWord.field,
		plusWord fieldTo twoWord.term,
		plusWord fieldTo term(
			ageWord.field,
			personWord.field))

	@Test
	fun string() {
		testTerm
			.string
			.assertEqualTo("one, negate, plus two, plus(age, person)")
	}

	@Test
	fun coreString() {
		testTerm
			.coreString
			.assertEqualTo("one()negate()plus(two())plus(age()person())")
	}

	@Test
	fun isSimple_singleWord() {
		oneWord.term.isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_wordChain() {
		oneWord.fieldTo(twoWord.term).term.isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_nodeChain() {
		term(oneWord.field, twoWord.field).isSimple.assertEqualTo(false)
	}
}