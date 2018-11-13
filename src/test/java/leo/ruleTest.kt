package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class RuleTest {
	@Test
	fun string() {
		term<Pattern>(oneWord)
			.returns(
				body(
					term(numberWord),
					identityFunction))
			.string
			.assertEqualTo("rule(term identifier word one, body(term identifier word number, function identity))")
	}

	@Test
	fun parse_withoutSelector() {
		term<Value>(
			defineWord fieldTo term(
				itWord fieldTo term(oneWord),
				isWord fieldTo term(numberWord)))
			.parseRule(identityFunction)
			.assertEqualTo(
				term<Nothing>(oneWord) returns body(term(numberWord), identityFunction))
	}

	@Test
	fun parse_withSelector() {
		term<Value>(
			defineWord fieldTo term(
				itWord fieldTo term(oneWord),
				isWord fieldTo term(thisWord)))
			.parseRule(identityFunction)
			.assertEqualTo(
				term<Pattern>(oneWord) returns body(term(selector()), identityFunction))
	}
}