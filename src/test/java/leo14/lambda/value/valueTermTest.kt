package leo14.lambda.value

import leo.base.assertEqualTo
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import kotlin.test.Test

class ValueTermTest {
	@Test
	fun native() {
		"foo".nativeValue.term.assertEqualTo(nativeTerm("foo"))
	}

	@Test
	fun function() {
		scope("a".nativeValue, "b".nativeValue)
			.function(nativeTerm("foo"))
			.value
			.term
			.assertEqualTo(
				fn(fn(fn(nativeTerm("foo"))))
					.invoke(nativeTerm("a"))
					.invoke(nativeTerm("b")))

	}
}