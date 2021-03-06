package leo19.compiler

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.script
import leo19.type.caseTo
import leo19.type.type
import leo19.typed.fieldTo
import leo19.typed.typed
import kotlin.test.Test

class SwitchCompilerTest {
	@Test
	fun static() {
		val switchBuilder = switchBuilder("zero" caseTo type())
		emptyResolver
			.switchCompiler(switchBuilder)
			.plus("zero" fieldTo script())
			.assertEqualTo(emptyResolver.switchCompiler(switchBuilder.plus("zero" fieldTo typed())))
	}
}