package vm3

import leo.base.println
import vm3.value.Value

data class Executor(
	val vm: Vm,
	val inputType: Type,
	val outputType: Type,
	val outputOffset: Offset
)

fun Executor.execute(data: Data): Data {
	if (data.type != inputType) error("${data.type.code} != ${inputType.code}")
	vm.data.set(0, data)
	vm.pc = 0
	vm.run()
	return vm.data.data(outputOffset.index(vm.data), outputType)
}

fun executor(function: Value.Function): Executor {
	val compiled = compile(function)
	return Executor(
		Vm(ByteArray(compiled.dataSize), compiled.code),
		function.param,
		compiled.outputType,
		compiled.outputOffset)
}

val Value.Function.executor get() = executor(this)

fun Offset.index(byteArray: ByteArray): Int =
	when (this) {
		is Offset.Direct -> index
		is Offset.Indirect -> byteArray.int(index)
	}

val Executor.dump get() = disassemble.println

val Executor.disassemble
	get() = """
	input: ${inputType.code}
  output: ${outputType.code}
	outputOffset: ${outputOffset.code}
  ${vm.disassemble}""".trimIndent()