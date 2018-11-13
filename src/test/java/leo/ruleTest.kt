package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class RuleTest {
	@Test
	fun string() {
		pattern(term(oneWord))
			.returns(
				body(
					template(term(numberWord)),
					identityFunction))
			.string
			.assertEqualTo("rule(pattern term identifier word one, body(template term identifier word number, function identity))")
	}

	@Test
	fun parse_withoutSelector() {
		script(
			term(
				defineWord fieldTo term(
					itWord fieldTo term(oneWord),
					isWord fieldTo term(numberWord))))
			.parseRule(identityFunction)
			.assertEqualTo(
				pattern(term(oneWord)) returns body(template(term(numberWord)), identityFunction))
	}

	@Test
	fun parse_withSelector() {
		script(
			term(
				defineWord fieldTo term(
					itWord fieldTo term(oneWord),
					isWord fieldTo term(thisWord))))
			.parseRule(identityFunction)
			.assertEqualTo(
				pattern(term(oneWord)) returns body(template(term(selector())), identityFunction))
	}
}