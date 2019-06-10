package leo5

data class Call(val body: Body)

fun call(body: Body) = Call(body)
fun Call.invoke(argument: Value) = (lhs as FunctionScript).function.body.invoke(argument)