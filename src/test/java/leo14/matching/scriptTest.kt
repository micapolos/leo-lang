package leo14.matching

import leo.base.assertEqualTo
import leo21.dsl.circle
import leo21.dsl.number
import leo21.dsl.radius
import leo21.dsl.shape
import leo21.dsl.side
import leo21.dsl.square
import kotlin.test.Test

class ScriptTest {
	@Test
	fun get() {
		circle(radius(number(10)))
			.radius
			.assertEqualTo(radius(number(10)))
	}

	@Test
	fun switch() {
		shape(circle(radius(number(10))))
			.switch(
				circle { it.radius },
				square { it.side })
			.assertEqualTo(radius(number(10)))
	}
}
