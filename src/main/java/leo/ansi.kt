package leo

object Ansi

val ansi = Ansi

val Ansi.escape get() = "\u001B["
val Ansi.clear get() = escape + "2J"
val Ansi.home get() = escape + "H"

val Ansi.black get() = escape + "30m"
val Ansi.red get() = escape + "31m"
val Ansi.green get() = escape + "32m"
val Ansi.yellow get() = escape + "33m"
val Ansi.blue get() = escape + "34m"
val Ansi.magenta get() = escape + "35m"
val Ansi.cyan get() = escape + "36m"
val Ansi.white get() = escape + "37m"
val Ansi.defaultColor get() = escape + "39m"

val Ansi.brightBlack get() = escape + "90m"
val Ansi.brightRed get() = escape + "91m"
val Ansi.brightGreen get() = escape + "92m"
val Ansi.brightYellow get() = escape + "93m"
val Ansi.brightBlue get() = escape + "94m"
val Ansi.brightMagenta get() = escape + "95m"
val Ansi.brightCyan get() = escape + "96m"
val Ansi.brightWhite get() = escape + "97m"

val Ansi.bold get() = escape + "1m"
val Ansi.italic get() = escape + "3m"
val Ansi.notBold get() = escape + "22m"
val Ansi.notItalic get() = escape + "23m"

val Ansi.reset get() = escape + "0m"

fun Ansi.goto(line: Int, column: Int) = "${escape}${line};${column}H"
val Ansi.eraseToEndOfLine get() = "${escape}K"