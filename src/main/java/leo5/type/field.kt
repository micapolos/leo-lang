package leo5.type

import leo5.*

data class Field(val name: String, val type: Type)

fun field(name: String, type: Type) = Field(name, type)

fun Field.compile(value: Value): Any = compile(value.script.application.onlyLine)
fun Field.compile(line: ValueLine): Any = type.compile(line.value(name))

