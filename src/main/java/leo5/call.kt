package leo5

data class Call(val function: Function)

fun call(function: Function) = Call(function)
fun Call.invoke(argument: Value) = (lhs as FunctionScript).function.invoke(argument)