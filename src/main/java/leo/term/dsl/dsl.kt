package leo.term.dsl

import leo.*
import leo.term.Application
import leo.term.application

typealias Line = Application<Nothing>

fun _script(line: Line, vararg lines: Line) = leo.term.term(line, *lines)

fun any(vararg lines: Line) = anyWord.application(*lines)
fun back(vararg lines: Line) = backWord.application(*lines)
fun bit(vararg lines: Line) = bitWord.application(*lines)
fun byte(vararg lines: Line) = byteWord.application(*lines)
fun either(vararg lines: Line) = eitherWord.application(*lines)
fun gives(vararg lines: Line) = givesWord.application(*lines)
fun natural(vararg lines: Line) = naturalWord.application(*lines)
fun number(vararg lines: Line) = numberWord.application(*lines)
fun of(vararg lines: Line) = ofWord.application(*lines)
fun one(vararg lines: Line) = oneWord.application(*lines)
fun plus(vararg lines: Line) = plusWord.application(*lines)
fun recurse(vararg lines: Line) = recurseWord.application(*lines)
fun the(vararg lines: Line) = theWord.application(*lines)
fun zero(vararg lines: Line) = zeroWord.application(*lines)
