package leo5.lang

import leo5.script.Code
import leo5.script.code
import leo5.script.span

operator fun Code.plus(code: Code) = code("plus", code)
operator fun Code.minus(code: Code) = code("minus", code)
operator fun Code.times(code: Code) = code("times", code)
operator fun Code.div(code: Code) = code("div", code)

fun float(double: Double) = float(span(double.toString()))
fun float(float: Float) = float(span(float.toString()))
fun float(int: Int) = float(span(int.toString()))

fun int(int: Int) = int(span(int.toString()))
