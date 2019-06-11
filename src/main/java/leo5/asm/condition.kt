package leo5.asm

data class Condition(val ptr: Ptr, val predicate: Predicate)

fun condition(ptr: Ptr, predicate: Predicate) = Condition(ptr, predicate)
fun Condition.invoke(runtime: Runtime) = predicate.test(runtime.memory.int(ptr))
