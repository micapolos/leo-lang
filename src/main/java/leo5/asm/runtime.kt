package leo5.asm

data class Runtime(
	val memory: Memory,
	val code: Code,
	val pc: Pc,
	val lhs: LhsBase,
	val rhs: RhsBase,
	val input: Input,
	val output: Output,
	var exit: Boolean = false)

fun Runtime.run() {
	exit = false
	do {
		step()
	} while (!exit)
}

fun Runtime.step() {
	fetch().invoke(this)
}

fun Runtime.fetch() =
	code.ops[pc.int++]

fun Runtime.getInt() =
	memory.getInt(lhs.base.ptr.int)

fun Runtime.set(ptr: Ptr, int: Int) =
	memory.set(ptr.int, int)

fun Runtime.set(ptr: Ptr, int: Int) =
	memory.set(ptr.int, int)

fun Runtime.intOp1(fn: Int.() -> Int) {
	memory.intOp1(lhs.base.ptr.int, fn)
}

fun Runtime.intOp2(fn: Int.(Int) -> Int) {
	memory.intOp2(lhs.base.ptr.int, rhs.base.ptr.int, fn)
}
