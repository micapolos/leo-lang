package leo14.dsl

import leo14.*

typealias X = Script

val _x = script()
fun _x(string: String, x: X = _x) = script(string fieldTo x)
fun X._x(string: String, x: X = _x) = plus(string fieldTo x)

fun it(string: String) = script(literal(string))
fun it(int: Int) = script(literal(number(int)))
fun it(double: Double) = script(literal(number(double)))

fun argument(x: X = _x) = _x("argument", x)
fun function(x: X = _x) = _x("function", x)
fun native(x: X = _x) = _x("native", x)
fun previous(x: X = _x) = _x("previous", x)
fun text(x: X = _x) = _x("text", x)
fun vec(x: X = _x) = _x("vec", x)
fun x(x: X = _x) = _x("x", x)
fun y(x: X = _x) = _x("y", x)
fun z(x: X = _x) = _x("z", x)

fun Script.apply(rhs: Script) = plus("apply" fieldTo rhs)
fun X.x(x: X = _x) = _x("x", x)
fun X.y(x: X = _x) = _x("y", x)
fun X.z(x: X = _x) = _x("z", x)

val argument = argument()
val previous = previous()

