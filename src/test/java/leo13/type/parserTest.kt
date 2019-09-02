package leo13.type

import leo13.script.assertScriptableLineWorks
import kotlin.test.Test

class ParserTest {
	@Test
	fun empty() {
		type().assertScriptableLineWorks { unsafeType }
	}

	@Test
	fun plain() {
		type(
			"zero" lineTo type(),
			"plus" lineTo type(
				"one" lineTo type()))
			.assertScriptableLineWorks { unsafeType }
	}

	@Test
	fun choice() {
		type(
			unsafeChoice(
				"zero" caseTo type(),
				"one" caseTo type(),
				"two" caseTo type()))
			.assertScriptableLineWorks { unsafeType }
	}

	@Test
	fun choiceAndLines() {
		type(
			unsafeChoice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.plus("two" lineTo type())
			.assertScriptableLineWorks { unsafeType }
	}

	@Test
	fun arrow() {
		type(
			arrow(
				type("zero"),
				type("one")))
			.assertScriptableLineWorks { unsafeType }
	}
}