package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class FunctionTest {
	val nameToStringFunction =
		Function(
			stack(
				rule(
					nameWord.term,
					body(
						stringWord.term,
						identityFunction))))

	@Test
	fun invoke_fallback() {
		Function(null)
			.invoke(oneWord.term)
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun invoke_single() {
		Function(
			stack(
				rule(nameWord.term(), body(stringWord.term(), identityFunction)),
				rule(ageWord.term(), body(numberWord.term(), identityFunction))))
			.invoke(nameWord.term)
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun invoke_chain() {
		Function(
			stack(
				nameWord.term returns
					body(
						stringWord.term,
						identityFunction),
				ageWord.term returns
					body(
						nameWord.term,
						Function(
							stack(
								nameWord.term returns
									body(
										stringWord.term,
										identityFunction))))))
			.invoke(ageWord.term)
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun invokeFallback_match() {
		nameToStringFunction
			.invoke(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term,
					nameWord.field))
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun invokeFallback_mismatch() {
		nameToStringFunction
			.invoke(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term,
					oneWord.field))
			.assertEqualTo(
				term(
					oneWord fieldTo term(
						nameWord fieldTo stringWord.term,
						ageWord fieldTo numberWord.term)))
	}

	@Test
	fun invokeFallback_notASelect() {
		nameToStringFunction
			.invoke(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
			.assertEqualTo(
				term(
					nameWord fieldTo stringWord.term,
					ageWord fieldTo numberWord.term))
	}
}