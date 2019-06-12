package leo5.asm

data class IntSetConst(val offset: Offset, val int: Int)

fun set(offset: Offset, int: Int) = IntSetConst(offset, int)

fun IntSetConst.invoke(runtime: Runtime) {
	runtime.set(offset, int)
}
