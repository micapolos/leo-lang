package leo15.type

import leo.base.assertEqualTo
import leo14.invoke
import leo15.isName
import kotlin.test.Test

class CompilerTest {
	@Test
	fun is_() {
		emptyLibrary
			.plus(type("foo").key bindingTo typed("bar").value)
			.compiler(typed("zoo"))
			.compile(isName("zar"()))
			.assertEqualTo(
				emptyLibrary
					.plus(type("foo").key bindingTo typed("bar").value)
					.plus(type("zoo").key bindingTo typed("zar").value)
					.compiler(emptyTyped))
	}
}