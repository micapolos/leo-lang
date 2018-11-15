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

	@Test
	fun reflect() {
		testTerm.reflect.string.assertEqualTo("term(field(word one, term null), field(word negate, term null), field(word plus, term field(word two, term null)), field(word plus, term(field(word age, term null), field(word person, term null))))")
	}
}