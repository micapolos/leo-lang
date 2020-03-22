package leo14.untyped.dsl2

import leo14.untyped.program

fun X.number(int: Int) = x(leo14.token(leo14.literal(int)))
fun X.number(double: Double) = x(leo14.token(leo14.literal(double)))
fun X.text(string: String) = x(leo14.token(leo14.literal(string)))

fun _run(v: V) = X.v()
fun _print(v: V) = _run(v).also { print(_reader.program) }
