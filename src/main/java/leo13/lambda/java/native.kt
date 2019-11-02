package leo13.lambda.java

data class Native(val code: Code)

fun native(code: Code) = Native(code)
fun native(int: Int) = native(code("$int"))
fun native(double: Double) = native(code("$double"))
fun native(string: String) = native(code("\"$string\""))

