package leo12

import leo.base.assertEqualTo
import leo.base.empty
import leo10.stringDict
import org.junit.Test

class PatternTest {
	@Test
	fun parse() {
		script(empty)
			.pattern
			.assertEqualTo(pattern(empty))

		script(scriptBody("one"))
			.pattern
			.assertEqualTo(pattern(body(patternExact("one"))))

		scriptBody("either")
			.patternBody
			.assertEqualTo(body(patternExact("either")))

		body("either" lineTo scriptBody("one"))
			.patternBody
			.assertEqualTo(body(patternChoice(stringDict("one" to pattern(empty)))))

		body(
			"either" lineTo scriptBody("one"),
			"either" lineTo scriptBody("two"))
			.patternBody
			.assertEqualTo(body(patternChoice(stringDict(
				"one" to pattern(empty),
				"two" to pattern(empty)))))

		body(
			"either" lineTo body("one" lineTo scriptBody("world")),
			"either" lineTo body("two" lineTo scriptBody("worlds")))
			.patternBody
			.assertEqualTo(body(patternChoice(stringDict(
				"one" to pattern(body(patternExact("world"))),
				"two" to pattern(body(patternExact("worlds")))))))
	}

	@Test
	fun matches() {
		pattern(empty).matches(script(empty)).assertEqualTo(true)

		body(patternChoice(stringDict(
			"one" to pattern(empty),
			"two" to pattern(empty))))
			.apply { matches(scriptBody("one")).assertEqualTo(true) }
			.apply { matches(scriptBody("two")).assertEqualTo(true) }
			.apply { matches(scriptBody("three")).assertEqualTo(false) }
	}
}