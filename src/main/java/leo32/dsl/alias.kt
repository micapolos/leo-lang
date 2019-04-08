package leo32.dsl

import leo32.runtime.Line

fun aClass(vararg xs: Line) = _class(*xs)
fun anEach(vararg xs: Line) = _for(*xs)
fun anElse(vararg xs: Line) = _else(*xs)
fun doesEqual(vararg xs: Line) = equals(*xs)
fun aFalse(vararg xs: Line) = _false(*xs)
fun aFun(vararg xs: Line) = _fun(*xs)
fun anIn(vararg xs: Line) = _in(*xs)
fun anInterface(vararg xs: Line) = _interface(*xs)
fun aTrue(vararg xs: Line) = _true(*xs)
