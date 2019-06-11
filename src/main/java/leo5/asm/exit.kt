package leo5.asm

object Exit

val exit = Exit

fun Exit.invoke(runtime: Runtime) {
	runtime.exit = true
}
