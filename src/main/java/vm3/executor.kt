package vm3

data class Executor(
	val vm: Vm,
	val inputType: Type,
	val outputType: Type
)

fun Executor.execute(data: Data): Data {
	vm.data.set(0, data)
	vm.pc = 0
	vm.run()
	return vm.data.data(vm.data.size - outputType.size, outputType)
}

fun executor(inputType: Type, outputValue: Value): Executor {
	val compiled = compile(inputType, outputValue)
	return Executor(Vm(ByteArray(compiled.dataSize), compiled.code), inputType, outputValue.type(inputType))
}
