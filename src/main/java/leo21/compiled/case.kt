package leo21.compiled

data class Case(val name: String, val fn: (Compiled) -> Compiled)

infix fun String.caseTo(fn: (Compiled) -> Compiled) = Case(this, fn)
