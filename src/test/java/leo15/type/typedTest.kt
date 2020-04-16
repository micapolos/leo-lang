package leo15.type

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedTest {
	@Test
	fun static() {
		emptyTyped.assertEqualTo(emptyTerm of emptyType)
		typed("zero").assertEqualTo(emptyTerm of type("zero"))
		typed("zero", "one").assertEqualTo(emptyTerm of type("zero", "one"))
		typed("point" lineTo typed("x", "y")).assertEqualTo(emptyTerm of type("point" lineTo type("x", "y")))
	}
}