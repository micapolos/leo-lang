package leo14.typed.compiler.js

import leo14.lineTo
import leo14.literal
import leo14.script

val helloWorld = script(literal("Hello, world!"))
val alert = script("javascript" lineTo script("alert" lineTo script(literal("Hello, world!"))))

fun main() = alert.show