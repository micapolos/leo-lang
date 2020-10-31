package leo20

import leo.base.assert
import leo14.script
import kotlin.test.Test

class PatternTest {
	@Test
	fun any() {
		value().matches(anyPattern).assert
		value(line(128)).matches(anyPattern).assert
	}

	@Test
	fun anyWithTail() {
		value(
			"head" lineTo value(),
			"tail" lineTo value())
			.matches(anyPattern.plus("tail" lineTo pattern()))
			.assert
	}

	@Test
	fun number() {
		value(128)
			.matches(pattern(numberPatternLine))
			.assert
	}

	@Test
	fun text() {
		value("ok")
			.matches(pattern(textPatternLine))
			.assert
	}

	@Test
	fun function() {
		value(line(emptyDictionary.function(body(script()))))
			.matches(pattern(functionPatternLine))
			.assert
	}


	@Test
	fun struct() {
		value(
			"x" lineTo value(line(10)),
			"y" lineTo value(line(20)))
			.matches(
				pattern(
					"x" lineTo pattern(numberPatternLine),
					"y" lineTo pattern(numberPatternLine)))
			.assert
	}
}