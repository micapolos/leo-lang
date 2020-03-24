package leo14.untyped.dsl2

import leo.base.runWith
import leo14.untyped.emptyReader

fun X.number(int: Int) = x(leo14.token(leo14.literal(int)))
fun X.number(double: Double) = x(leo14.token(leo14.literal(double)))
fun X.text(string: String) = x(leo14.token(leo14.literal(string)))

fun run_(f: F) = readerParameter.runWith(emptyReader) { X.f() }
fun library_(f: F) = run_(f) // TODO
