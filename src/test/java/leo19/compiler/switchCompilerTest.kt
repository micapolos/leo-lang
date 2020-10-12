package leo19.compiler

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.script
import leo19.type.caseTo
import leo19.type.struct
import leo19.typed.fieldTo
import leo19.typed.typed
import kotlin.test.Test

class SwitchCompilerTest {
	@Test
	fun add() {
		val switchBuilder = switchBuilder("zero" caseTo struct())
		emptyResolver
			.switchCompiler(switchBuilder)
			.plus("zero" fieldTo script())
			.assertEqualTo(emptyResolver.switchCompiler(switchBuilder.plus("zero" fieldTo typed())))
	}
}