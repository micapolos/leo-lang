package leo13.type

import leo13.script.assertScriptableLineWorks
import kotlin.test.Test

class ParserTest {
	@Test
	fun empty() {
		pattern().assertScriptableLineWorks { unsafePattern }
	}

	@Test
	fun plain() {
		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern(
				"one" lineTo pattern()))
			.assertScriptableLineWorks { unsafePattern }
	}

	@Test
	fun choice() {
		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern(),
				"two" caseTo pattern()))
			.assertScriptableLineWorks { unsafePattern }
	}

	@Test
	fun choiceLines() {
		pattern(
			"zero" lineTo pattern(),
			"or" lineTo pattern("one" lineTo pattern()),
			"or" lineTo pattern("two" lineTo pattern()))
			.assertScriptableLineWorks { unsafePattern }
	}

	@Test
	fun choiceAndLines() {
		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.plus("two" lineTo pattern())
			.assertScriptableLineWorks { unsafePattern }
	}

	@Test
	fun arrow() {
		pattern(
			arrow(
				type(pattern("zero")),
				type(pattern("one"))))
			.assertScriptableLineWorks { unsafePattern }
	}
}