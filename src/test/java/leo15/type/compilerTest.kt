package leo15.type

import leo.base.assertEqualTo
import leo14.invoke
import leo15.isName
import kotlin.test.Test

class CompilerTest {
	@Test
	fun is_() {
		emptyLibrary
			.compiler(typed("foo"))
			.compile(isName("bar"()))
			.assertEqualTo(
				emptyLibrary
					.plus(type("foo").key bindingTo typed("bar").value)
					.compiler(emptyTyped))
	}
}