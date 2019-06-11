package leo5.asm

data class Jump(val int: Int)

fun jump(int: Int) = Jump(int)
fun Jump.invoke(runtime: Runtime) {
	runtime.pc.int = int
}
