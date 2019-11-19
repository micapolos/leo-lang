package leo

object Ansi

val ansi = Ansi

val Ansi.escape get() = "\u001B["
val Ansi.clear get() = escape + "2J"
val Ansi.home get() = escape + "H"

val Ansi.red get() = escape + "31m"
val Ansi.reset get() = escape + "0m"
