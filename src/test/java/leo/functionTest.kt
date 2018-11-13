package leo

import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class FunctionTest {
	@Test
	fun invoke_fallback() {
		Function(null)
			.invoke(term(oneWord))
			.assertEqualTo(term(oneWord))
	}

	@Test
	fun invoke_single() {
		Function(
			stack(
				term<Pattern>(nameWord) returns body(term(stringWord), identityFunction),
				term<Pattern>(ageWord) returns body(term(numberWord), identityFunction)))
			.invoke(term(nameWord))
			.assertEqualTo(term(stringWord))
	}

	@Test
	fun invoke_chain() {
		Function(
			stack(
				term<Pattern>(nameWord) returns
					body(
						term(stringWord),
						identityFunction),
				term<Pattern>(ageWord) returns
					body(
						term(nameWord),
						Function(
							stack(
								term<Pattern>(nameWord) returns
									body(
										term(stringWord),
										identityFunction))))))
			.invoke(term(ageWord))
			.assertEqualTo(term(stringWord))
	}
}