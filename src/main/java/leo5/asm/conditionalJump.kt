package leo5.asm

data class ConditionalJump(val jump: Jump, val condition: Condition)

fun ConditionalJump.invoke(runtime: Runtime) {
	if (condition.invoke(runtime)) jump.invoke(runtime)
}