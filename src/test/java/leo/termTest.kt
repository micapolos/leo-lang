package leo

import leo.base.assertEqualTo
import leo.base.stack
import leo.base.string
import kotlin.test.Test

val personTerm =
	term(
		personWord fieldTo term(
			nameWord fieldTo term(
				firstWord fieldTo metaTerm("Michał"),
				lastWord fieldTo metaTerm("Pociecha-Łoś")),
			ageWord fieldTo metaTerm("42")))

class TermTest {
	@Test
	fun string() {
		personTerm
			.string
			.assertEqualTo("person(name(first \"Michał\", last \"Pociecha-Łoś\"), age \"42\")")
	}

	@Test
	fun reflect() {
		personTerm
			.reflect { term(stringWord) }
			.string
			.assertEqualTo("term structure " +
				"field(key word person, value term structure(" +
				"field(key word name, value term structure(" +
				"field(key word first, value term meta string), " +
				"field(key word last, value term meta string))), " +
				"field(key word age, value term meta string)))")
	}

	val termForGet = term(
		oneWord fieldTo metaTerm(1),
		ageWord fieldTo metaTerm(42),
		ageWord fieldTo metaTerm(43),
		twoWord fieldTo metaTerm(2)
	)

	@Test
	fun only() {
		termForGet.only(oneWord).assertEqualTo(metaTerm(1))
		termForGet.only(twoWord).assertEqualTo(metaTerm(2))
		termForGet.only(ageWord).assertEqualTo(null)
		termForGet.only(nameWord).assertEqualTo(null)
	}

	@Test
	fun all() {
		termForGet.all(oneWord).assertEqualTo(stack(metaTerm(1)))
		termForGet.all(twoWord).assertEqualTo(stack(metaTerm(2)))
		termForGet.all(ageWord).assertEqualTo(stack(metaTerm(42), metaTerm(43)))
		termForGet.all(nameWord).assertEqualTo(null)
	}

	@Test
	fun nullPushIdentifier() {
		(null as Term<Nothing>?)
			.push(oneWord)
			.assertEqualTo(term(oneWord))
	}

	@Test
	fun nativePushIdentifier() {
		metaTerm(1)
			.push(oneWord)
			.assertEqualTo(null)
	}

	@Test
	fun idPushId() {
		term<Nothing>(oneWord)
			.push(twoWord)
			.assertEqualTo(null)
	}

	@Test
	fun listPushWord() {
		term(oneWord fieldTo metaTerm(1))
			.push(twoWord)
			.assertEqualTo(null)
	}

	@Test
	fun nullPushField() {
		(null as Term<Nothing>?)
			.push(oneWord fieldTo term(numberWord))
			.assertEqualTo(term(oneWord fieldTo term(numberWord)))
	}

	@Test
	fun nativePushField() {
		metaTerm(1)
			.push(twoWord fieldTo metaTerm(2))
			.assertEqualTo(null)
	}

	@Test
	fun wordPushField() {
		term<Nothing>(oneWord)
			.push(twoWord fieldTo metaTerm(2))
			.assertEqualTo(null)
	}

	@Test
	fun listPushField() {
		term(oneWord fieldTo metaTerm(1))
			.push(twoWord fieldTo metaTerm(2))
			.assertEqualTo(
				term(
					oneWord fieldTo metaTerm(1),
					twoWord fieldTo metaTerm(2)))
	}

	@Test
	fun selectSingle() {
		termForGet
			.select(oneWord)
			.assertEqualTo(metaTerm(1))
	}

	@Test
	fun selectMultiple() {
		termForGet
			.select(ageWord)
			.assertEqualTo(
				term(
					previousWord fieldTo term(
						lastWord fieldTo metaTerm(42)),
					lastWord fieldTo metaTerm(43)))
	}

	@Test
	fun selectMissing() {
		termForGet
			.select(personWord)
			.assertEqualTo(null)
	}
}