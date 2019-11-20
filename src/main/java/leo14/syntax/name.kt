package leo14.syntax

data class Name(val string: String, val syntax: Syntax)
fun name(string: String, syntax: Syntax) = Name(string, syntax)