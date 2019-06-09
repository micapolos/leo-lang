package leo5

data class Apply(val function: Function, val op: Op)

fun apply(function: Function, op: Op) = Apply(function, op)
fun Apply.invoke(argument: Value) = op.invoke(function.invoke(argument), argument)
