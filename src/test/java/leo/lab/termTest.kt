package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	val testTerm = nullTerm
		.plus(oneWord)
		.plus(negateWord)
		.plus(plusWord, nullTerm
			.plus(twoWord))
		.plus(plusWord, nullTerm
			.plus(ageWord)
			.plus(personWord))

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
		nullTerm.plus(oneWord).isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_wordChain() {
		nullTerm.plus(oneWord, nullTerm.plus(twoWord)).isSimple.assertEqualTo(true)
	}

	@Test
	fun isSimple_nodeChain() {
		nullTerm.plus(oneWord).plus(twoWord).isSimple.assertEqualTo(false)
	}
}