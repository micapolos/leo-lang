package leo15.type

import kotlin.test.Test

class TypedTest {
	@Test
	fun empty() {
		emptyTyped.assertEvalsTo(emptyTyped)
		typed("foo").assertEvalsTo(typed("foo"))
		typed("foo", "bar").assertEvalsTo(typed("foo", "bar"))
	}

	@Test
	fun linkOrNull() {
		typed("zero")
			.linkOrNull!!
			.typed
			.assertEvalsTo(typed("zero"))
	}
}