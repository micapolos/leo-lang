package leo25

import leo.base.assertEqualTo
import kotlin.test.Test

class ContextTest {
	@Test
	fun applyString() {
		context()
			.plus(value("ping"), binding(value("pong")))
			.applyOrNull(value("ping"))
			.assertEqualTo(value("pong"))
	}

	@Test
	fun applyWord() {
		context()
			.plus(value("ping" to null), binding(value("ok")))
			.run {
				applyOrNull(value("ping" to null)).assertEqualTo(value("ok"))
				applyOrNull(value("pong" to null)).assertEqualTo(null)
				applyOrNull(value("pong" to null, "ping" to null)).assertEqualTo(null)
			}
	}

	@Test
	fun applyStruct() {
		context()
			.plus(value("name" to anyValue), binding(value("ok")))
			.run {
				applyOrNull(value("name" to null)).assertEqualTo(null)
				applyOrNull(value("name" to value("michal" to null))).assertEqualTo(value("ok"))
				applyOrNull(value("name" to value("Michał"))).assertEqualTo(value("ok"))
			}
	}

	@Test
	fun applyAny() {
		context()
			.plus(anyValue, binding(value("pong")))
			.run {
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
				applyOrNull(value("x" to null)).assertEqualTo(value("pong"))
				applyOrNull(value("x" to value("10"))).assertEqualTo(value("pong"))
			}
	}

	@Test
	fun anyValueApply() {
		context()
			.plus(anyValue.plus("plus" to anyValue), binding(value("ok")))
			.run {
				applyOrNull(value("a" to null, "plus" to value("b" to null)))
					.assertEqualTo(value("ok"))
				applyOrNull(value("plus" to value("b" to null)))
					.assertEqualTo(null)
				applyOrNull(value("a" to null, "plus" to null))
					.assertEqualTo(null)
				applyOrNull(value("plus" to null))
					.assertEqualTo(null)
			}
	}
}