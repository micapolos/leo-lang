package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class ScopeTest {
	val nameToStringRule =
		rule(
			nameWord.term,
			body(
				stringWord.term,
				identityFunction))

	val nameToStringFunction =
		Function(stack(nameToStringRule))

	@Test
	fun evaluate_define() {
		Scope(
			nameToStringFunction,
			term(
				defineWord fieldTo term(
					itWord fieldTo term(ageWord.field),
					isWord fieldTo term(numberWord.field))))
			.evaluate
			.assertEqualTo(
				Scope(
					Function(
						stack(
							nameToStringRule,
							rule(
								ageWord.term,
								body(
									numberWord.term,
									nameToStringFunction)))),
					null))
	}

	@Test
	fun evaluate_invoke() {
		Scope(
			nameToStringFunction,
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
			nameToStringFunction,
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