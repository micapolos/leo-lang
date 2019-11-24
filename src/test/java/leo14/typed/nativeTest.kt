package leo14.typed

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.literal
import leo14.native.doublePlusDoubleNative
import leo14.native.native
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
			numberName fieldTo nativeTyped(native(2)),
			"plus" fieldTo typed(
				numberName fieldTo nativeTyped(native(3))))

		typed
			.nativeResolve
			.assertEqualTo(
				term(doublePlusDoubleNative)
					.invoke(typed.lineLink.tail.term)
					.invoke(typed.lineLink.head.term) of numberType)
	}

	@Test
	fun evalNativeFields() {
		val typed = typed(
			"x" lineTo typed(native(1)),
			"y" lineTo typed(native(2)))
		typed.eval.assertEqualTo(typed)
	}
}