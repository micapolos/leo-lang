package leo13

data class Name(val string: String)

fun name(string: String) = Name(string)
val String.nameAsScriptLine get() = "name" lineTo script(this lineTo script())