package leo5

import leo.base.fold

data class BodyDictionary(val map: Map<String, Body>)

fun bodyDictionary() = BodyDictionary(emptyMap())
fun dictionary(line: BodyLine, vararg lines: BodyLine) = bodyDictionary().fold(line, lines) { plus(it) }
fun BodyDictionary.plus(line: BodyLine) = BodyDictionary(map.plus(line.name to line.body))
fun BodyDictionary.at(name: String) = map.getValue(name)
