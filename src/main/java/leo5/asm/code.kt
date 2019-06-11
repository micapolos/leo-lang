package leo5.asm

class Code(var ops: Array<out Op>)

fun code(vararg ops: Op) = Code(ops)
val newCode get() = Code(arrayOf())
