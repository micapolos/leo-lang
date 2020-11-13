package leo14.matching

import leo.base.assertEqualTo
import leo22.dsl.circle
import leo22.dsl.number
import leo22.dsl.radius
import leo22.dsl.shape
import leo22.dsl.side
import leo22.dsl.square
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
