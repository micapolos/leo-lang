package leo14.typed

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.literal
import leo14.native.intPlusIntNative
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
			"int" fieldTo nativeTyped(native(2)),
			"plus" fieldTo typed(
				"int" fieldTo nativeTyped(native(3))))

		typed
			.nativeResolve
			.assertEqualTo(
				term(intPlusIntNative)
					.invoke(typed.lineLink.tail.term)
					.invoke(typed.lineLink.head.term) of type("int" fieldTo nativeType))
	}

	@Test
	fun nativeEval() {
		typed(native(123))
			.plus("plus" lineTo typed(native(123)))
			.nativeResolve
			.assertEqualTo(null)
	}
}