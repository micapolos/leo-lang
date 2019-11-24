package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.native.native
import leo14.typed.compiler.natives.context
import leo14.typed.lineTo
import leo14.typed.plus
import leo14.typed.resolve
import leo14.typed.typed
import kotlin.test.Test

class CompiledTest {
	@Test
	fun resolve() {
		val compiled = compiled(typed(native(0)))
		compiled
			.resolve("x" lineTo typed(), context)
			.assertEqualTo(compiled.updateTyped { plus("x" lineTo typed()).resolve!! })
	}
}