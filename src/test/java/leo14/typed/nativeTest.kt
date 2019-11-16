package leo14.typed

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.native.Native
import leo14.native.intPlusIntNative
import leo14.native.native
import kotlin.test.Test

class NativeTest {
	@Test
	fun nativeResolve() {
		val typed = typed<Native>()
			.plusNative(term(native(2)))
			.plus("plus" fieldTo typed<Native>().plusNative(term(native(2))))

		typed
			.nativeResolve
			.assertEqualTo(
				term(intPlusIntNative)
					.invoke(typed.lineLink.tail.term)
					.invoke(typed.lineLink.head.term) of nativeType)
	}
}