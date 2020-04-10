package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.untyped.nativeName
import kotlin.test.Test

class TypeTest {
	@Test
	fun staticOrNull() {
		type(nativeName lineTo emptyType)
			.staticOrNull
			.assertEqualTo(nativeType)
	}
}