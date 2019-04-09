package leo32.dsl

import leo.base.fold
import leo32.runtime.Line
import leo32.runtime.line
import leo32.runtime.plus
import leo32.runtime.script

fun aClass(vararg xs: Line) = _class(*xs)
fun anEach(vararg xs: Line) = _for(*xs)
fun anElse(vararg xs: Line) = _else(*xs)
fun doesEqual(vararg xs: Line) = equals(*xs)
fun aFalse(vararg xs: Line) = _false(*xs)
fun aFun(vararg xs: Line) = _fun(*xs)
fun anIn(vararg xs: Line) = _in(*xs)
fun anInterface(vararg xs: Line) = _interface(*xs)
fun aTrue(vararg xs: Line) = _true(*xs)

fun _script(vararg lines: Line) =
	leo32.runtime.script(*lines)
fun _line(string: String) =
	line(string)
fun _eval(vararg lines: Line) =
	leo32.runtime.term().fold(lines) { this.plus(it) }.script

fun boolean(boolean: Boolean) = boolean(line("$boolean"))
fun byte(byte: Byte) = byte(line("$byte"))
fun short(short: Short) = short(line("$short"))
fun int(int: Int) = int(line("$int"))
fun long(long: Long) = long(line("$long"))
fun float(float: Float) = float(line("$float"))
fun double(double: Double) = double(line("$double"))
fun string(string: String) = string(line("\"$string\"")) // TODO: Escape
fun char(char: Char) = char(line("\'$char\'")) // TODO: Escape
