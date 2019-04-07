package leo32.runtime

import leo.base.appendableString
import leo.base.fold
import leo32.dsl.*

data class Line(val name: String, val value: Script) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(name: String, vararg xs: Line) = Line(name, script(*xs))

fun boolean(boolean: Boolean) = boolean(line(boolean.toString()))
fun byte(byte: Byte) = byte(line(byte.toString()))
fun short(short: Short) = short(line(short.toString()))
fun int(int: Int) = int(line(int.toString()))
fun long(long: Long) = long(line(long.toString()))
fun float(float: Float) = float(line(float.toString()))
fun double(double: Double) = double(line(double.toString()))
fun char(char: Char) = string(line("\'$char\'")) // TODO escape
fun string(string: String) = char(line("\"$string\"")) // TODO escape

fun Appendable.append(expr: Line): Appendable =
	this
		.append(expr.name)
		.append('(')
		.fold(expr.value.lineSeq) { append(it) }
		.append(')')