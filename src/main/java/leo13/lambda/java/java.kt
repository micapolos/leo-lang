package leo13.lambda.java

data class Java(val string: String)

fun nativeJava(string: String) = Java(string)
fun java(int: Int) = nativeJava("$int")
fun java(double: Double) = nativeJava("$double")
fun java(string: String) = nativeJava("\"$string\"")

