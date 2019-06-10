package leo5

data class Apply(val body: Body, val op: Op)

fun apply(body: Body, op: Op) = Apply(body, op)
fun Apply.invoke(argument: Value) = op.invoke(body.invoke(argument), argument)
