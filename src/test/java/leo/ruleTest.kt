package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class RuleTest {
	@Test
	fun string() {
		pattern(term(oneWord))
			.returns(template(term(numberWord)))
			.string
			.assertEqualTo("rule(pattern term identifier word one, template term identifier word number)")
	}

	@Test
	fun parse_withoutSelector() {
		script(
			term(
				defineWord fieldTo term(
					itWord fieldTo term(oneWord),
					isWord fieldTo term(numberWord))))
			.parseRule
			.assertEqualTo(
				pattern(term(oneWord)) returns template(term(numberWord)))
	}

	@Test
	fun parse_withSelector() {
		script(
			term(
				defineWord fieldTo term(
					itWord fieldTo term(oneWord),
					isWord fieldTo term(thisWord))))
			.parseRule
			.assertEqualTo(
				pattern(term(oneWord)) returns template(term(selector())))
	}
}