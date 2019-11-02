package leo13.lambda.java

data class Java(val code: Code)

fun java(code: Code) = Java(code)
fun java(int: Int) = java(code("$int"))
fun java(double: Double) = java(code("$double"))
fun java(string: String) = java(code("\"$string\""))

