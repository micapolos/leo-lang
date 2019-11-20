package leo14.syntax

data class Name(val string: String, val syntax: Kind)

fun name(string: String, syntax: Kind) = Name(string, syntax)