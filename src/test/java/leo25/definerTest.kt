package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class DefinerTest {
	@Test
	fun defineDo() {
		context()
			.define(
				script(
					"name" lineTo script(anyName),
					doName lineTo script("name")
				)
			)
			.assertEqualTo(
				context()
					.plus(
						script("name" lineTo script(anyName)),
						binding(context().function(body(script("name"))))
					)
			)
	}

	@Test
	fun letBe() {
		context()
			.define(
				script(
					"name" lineTo script(anyName),
					beName lineTo script("name")
				)
			)
			.assertEqualTo(
				context()
					.plus(
						script(getName lineTo script("name" lineTo script(anyName))),
						binding(value("name"))
					)
			)
	}
}