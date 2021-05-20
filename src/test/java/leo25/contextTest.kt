package leo25

import kotlinx.collections.immutable.persistentMapOf
import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ContextTest {
	@Test
	fun plusAny() {
		context()
			.plus(script("any"), binding(value("ok")))
			.assertEqualTo(
				Context(persistentMapOf(token(anyEnd) to resolution(binding(value("ok")))))
			)
	}

	@Test
	fun applyString() {
		context()
			.plus(script("ping"), binding(value("pong")))
			.applyOrNull(value("ping"))
			.assertEqualTo(value("pong"))
	}

	@Test
	fun applyStruct() {
		context()
			.plus(script("name" lineTo script("any")), binding(value("ok")))
			.run {
				applyOrNull(value("name" lineTo value())).assertEqualTo(value("ok"))
				applyOrNull(value("name" lineTo value("michal"))).assertEqualTo(value("ok"))
				applyOrNull(value("name" lineTo value(line(literal("Michał"))))).assertEqualTo(value("ok"))
			}
	}

	@Test
	fun applyAny() {
		context()
			.plus(script("any"), binding(value("pong")))
			.run {
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
			}
	}

	@Test
	fun anyValueApply() {
		context()
			.plus(
				script("any" lineTo script(), "plus" lineTo script("any")),
				binding(value("ok"))
			)
			.run {
				applyOrNull(value("a" lineTo value(), "plus" lineTo value("b" lineTo value())))
					.assertEqualTo(value("ok"))
			}
	}
}