package vm3

import leo.base.assertEqualTo
import vm3.dsl.input
import vm3.dsl.intDec
import vm3.dsl.intInc
import vm3.dsl.intPlus
import kotlin.test.Test

class CompilerTest {
	@Test
	fun test() {
		val compiled = compile(
			Type.I32,
			input.intInc.intPlus(input.intDec))

		compiled
			.disassemble
		//.assertEqualTo("")

		val vm = Vm(
			ByteArray(compiled.dataSize),
			compiled.code)

		vm.run()
	}
}