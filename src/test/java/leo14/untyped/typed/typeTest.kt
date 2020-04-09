package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.untyped.numberName
import kotlin.test.Test

class TypeTest {
	@Test
	fun staticOrNull() {
		type(numberName lineTo emptyType)
			.staticOrNull
			.assertEqualTo(numberType)
	}
}