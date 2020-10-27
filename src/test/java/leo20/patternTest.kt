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
	fun number() {
		value(line(128))
			.matches(pattern("number" fieldTo anyPattern))
			.assert
	}

	@Test
	fun text() {
		value(line("ok"))
			.matches(pattern("text" fieldTo anyPattern))
			.assert
	}

	@Test
	fun function() {
		value(line(emptyScope.function(script())))
			.matches(pattern("function" fieldTo anyPattern))
			.assert
	}


	@Test
	fun struct() {
		value(
			"x" lineTo value(line(10)),
			"y" lineTo value(line(20)))
			.matches(
				pattern(
					"x" fieldTo pattern("number" fieldTo anyPattern),
					"y" fieldTo pattern("number" fieldTo anyPattern)))
			.assert
	}
}