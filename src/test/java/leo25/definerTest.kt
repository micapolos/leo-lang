package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class DefinerTest {
	@Test
	fun defineDo() {
		dictionary()
			.define(
				script(
					"name" lineTo script(anyName),
					doName lineTo script("name")
				)
			)
			.assertEqualTo(
				dictionary()
					.plus(
						script("name" lineTo script(anyName)),
						binding(dictionary().function(body(script("name"))))
					)
			)
	}

	@Test
	fun letBe() {
		dictionary()
			.define(
				script(
					"name" lineTo script(anyName),
					beName lineTo script("name")
				)
			)
			.assertEqualTo(
				dictionary()
					.plus(
						script(getName lineTo script("name" lineTo script(anyName))),
						binding(value("name"))
					)
			)
	}
}