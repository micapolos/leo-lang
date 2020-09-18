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

fun executor(fn: Fn): Executor {
	val compiled = compile(fn)
	return Executor(Vm(ByteArray(compiled.dataSize), compiled.code), fn.input, compiled.outputType)
}

val Fn.executor get() = executor(this)