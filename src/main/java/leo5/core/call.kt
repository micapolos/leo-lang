package leo5.core

data class Call(val function: Function, val parameter: Function)

fun call(function: Function, parameter: Function) = Call(function, parameter)
fun Call.invoke(value: Value) = function.invoke(value).invoke(parameter.invoke(value))