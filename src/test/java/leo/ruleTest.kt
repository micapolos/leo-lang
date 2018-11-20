package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class RuleTest {
//	@Test
//	fun string() {
//		rule(
//			oneWord.term(),
//			body(
//				numberWord.term,
//				emptyFunction))
//			.string
//			.assertEqualTo("rule(" +
//				"term field(word one, term null), " +
//				"body(term field(word number, term null), function identity))")
//	}

	@Test
	fun parse_withoutSelector() {
		term(
			defineWord fieldTo term(
				itWord fieldTo oneWord.term,
				isWord fieldTo numberWord.term))
			.parseRule(emptyFunction)
			.assertEqualTo(
				rule(oneWord.term(), body(numberWord.term(), emptyFunction)))
	}

	@Test
	fun parse_withSelector() {
		term(
			defineWord fieldTo term(
				itWord fieldTo oneWord.term,
				isWord fieldTo thisWord.term))
			.parseRule(emptyFunction)
			.assertEqualTo(
				rule(oneWord.term(), body(selector().metaTerm, emptyFunction)))
	}
}