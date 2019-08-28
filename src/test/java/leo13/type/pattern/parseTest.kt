package leo13.type.pattern

import leo13.script.assertScriptableLineWorks
import kotlin.test.Test

class ParseTest {
	@Test
	fun empty() {
		type().assertScriptableLineWorks { typeOrNull }
	}

	@Test
	fun plain() {
		type(
			"zero" lineTo type(),
			"plus" lineTo type(
				"one" lineTo type()))
			.assertScriptableLineWorks { typeOrNull }
	}

	@Test
	fun choice() {
		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type(),
				"two" caseTo type()))
			.assertScriptableLineWorks { typeOrNull }
	}

	@Test
	fun choiceLines() {
		type(
			"zero" lineTo type(),
			"or" lineTo type("one" lineTo type()),
			"or" lineTo type("two" lineTo type()))
			.assertScriptableLineWorks { typeOrNull }
	}

	@Test
	fun choiceAndLines() {
		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.plus("two" lineTo type())
			.assertScriptableLineWorks { typeOrNull }
	}

	@Test
	fun arrow() {
		type(
			arrow(
				type("zero"),
				type("one")))
			.assertScriptableLineWorks { typeOrNull }
	}
}