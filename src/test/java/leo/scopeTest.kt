package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class ScopeTest {
	private val nameToStringFunction =
		emptyFunction.define(nameWord.term(), body(stringWord.term(), emptyFunction))

	@Test
	fun evaluate_define() {
		Scope(
			nameToStringFunction!!,
			term(
				ageWord.field,
				isWord fieldTo term(numberWord.field)))
			.evaluate
			.assertEqualTo(
				Scope(
					nameToStringFunction
						.define(
							ageWord.term(),
							body(
								numberWord.term,
								nameToStringFunction))!!,
					null))
	}

	@Test
	fun evaluate_defineEmpty() {
		Scope(
			nameToStringFunction!!,
			term(
				ageWord.field,
				isWord.field))
			.evaluate
			.assertEqualTo(
				Scope(
					nameToStringFunction
						.define(
							ageWord.term(),
							body(
								null,
								nameToStringFunction))!!,
					null))
	}

	@Test
	fun evaluate_invoke() {
		Scope(
			nameToStringFunction!!,
			nameWord.term)
			.evaluate
			.assertEqualTo(
				Scope(
					nameToStringFunction,
					stringWord.term))
	}

	@Test
	fun evaluate_select() {
		Scope(
			nameToStringFunction!!,
			term(
				nameWord fieldTo stringWord.term,
				ageWord fieldTo numberWord.term,
				nameWord.field))
			.evaluate
			.assertEqualTo(
				Scope(
					nameToStringFunction,
					stringWord.term))
	}
}