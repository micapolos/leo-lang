package leo14.untyped.dsl2

import leo.base.runWith
import leo14.untyped.Reader
import leo14.untyped.Resolver
import leo14.untyped.emptyReader
import leo14.untyped.rootResolverOrNull

fun X.number(int: Int) = x(leo14.token(leo14.literal(int)))
fun X.number(double: Double) = x(leo14.token(leo14.literal(double)))
fun X.text(string: String) = x(leo14.token(leo14.literal(string)))
val nothing_ = X

fun run_(f: F): Unit = readerParameter.runWith(emptyReader) { X.f() }
fun resolver_(f: F): Resolver = run_(f).run { readerParameter.value.rootResolverOrNull!! }
fun library_(f: F) = f
fun Reader.read(f: F): Reader = readerParameter.runWith(this) { X.f(); readerParameter.value }