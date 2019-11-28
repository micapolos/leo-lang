package leo14.typed.compiler.js

import leo14.typed.*

val javascriptName = "javascript"
val expressionName = "expression"
val statementName = "statement"

fun javascriptLine(type: Type) = javascriptName lineTo type
fun javascriptType(type: Type) = type(javascriptLine(type))
fun javascriptType(line: Line) = javascriptType(type(line))

val expressionLine = line(objectLineNative)
val expressionType = type(expressionLine)

