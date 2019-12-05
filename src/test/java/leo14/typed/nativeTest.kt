package leo14.typed

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.literal
import leo14.native.native
import leo14.native.numberPlusNumberNative
import leo14.typed.compiler.natives.*
import kotlin.test.Test

class NativeTest {
	@Test
	fun literalNativeTypedLine() {
		literal(123)
			.nativeTypedLine
			.assertEqualTo(typedLine(native(123)))
	}

	@Test
	fun nativeResolve() {
		val typed = typed(
			term(native(2)) of numberLine,
			"plus" lineTo typed(
				term(native(3)) of numberLine))

		typed
			.nativeResolve
			.assertEqualTo(
				typed.resolveLink { link ->
					term(numberPlusNumberNative)
						.invoke(link.tail.term)
						.invoke(link.head.term) of numberType
				})
	}

	@Test
	fun evalNativeFields() {
		val typed = typed(
			"x" lineTo typed(native(1)),
			"y" lineTo typed(native(2)))
		typed.eval.assertEqualTo(typed)
	}
}