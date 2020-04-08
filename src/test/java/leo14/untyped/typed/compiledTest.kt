package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.ifOrNull
import leo14.lambda.runtime.Fn
import leo14.lambda.runtime.fn
import leo14.lib.number
import kotlin.test.Test

@Suppress("UNCHECKED_CAST")
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

	@Test
	fun matchFunction() {
		textType
			.functionTo(numberType)
			.type
			.compiled { fn { (it as String).length.number } }
			.matchFunction { function, valueFn ->
				ifOrNull(function.from == textType) {
					function.to.compiled { (valueFn(null) as Fn)("Hello, world!") }
				}
			}!!
			.typed
			.assertEqualTo(numberType.typed(13.number))
	}
}