package leo14.untyped

data class Constant(val program: Program)

fun constant(program: Program) = Constant(program)
val Program.constant get() = Constant(this)