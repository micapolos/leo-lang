package leo14.lambda.java

import leo14.lambda.code.Code
import leo14.lambda.code.code

data class Native(val code: Code)

fun native(code: Code) = Native(code)
fun native(int: Int) = native(code("$int"))
fun native(double: Double) = native(code("$double"))
fun native(string: String) = native(code("\"$string\""))
