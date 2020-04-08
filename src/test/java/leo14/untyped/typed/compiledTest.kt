package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class CompiledTest {
	@Test
	fun matchInfix() {
		textType
			.plus("and" lineTo textType)
			.compiled { "Hello, " to "world!" }
			.matchInfix("and") { lhs, rhs ->
				textType.compiled { (lhs.value as String) + (rhs.value as String) }
			}!!
			.typed
			.assertEqualTo(textType.typed("Hello, world!"))
	}

	@Test
	fun matchInfix_nameMismatch() {
		textType
			.plus("and" lineTo textType)
			.compiled { null!! }
			.matchInfix("or") { _, _ -> null!! }
			.assertNull
	}
}