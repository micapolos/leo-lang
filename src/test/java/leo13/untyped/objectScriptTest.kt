package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeValue
import org.junit.Test

class ObjectScriptTest {
	@Test
	fun reader() {
		objectScriptReader
			.unsafeValue("script" lineTo script())
			.assertEqualTo(script)
	}
}