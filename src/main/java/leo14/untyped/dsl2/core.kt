package leo14.untyped.dsl2

import leo14.Token
import leo14.end
import leo14.untyped.emptyReader
import leo14.untyped.write

var _reader = emptyReader

object X
typealias V = X.() -> X

fun X.x(token: Token) = also { _reader = _reader.write(token)!! }
fun X.x(name: String, v: V) = x(leo14.token(leo14.begin(name))).v().x(leo14.token(end))
fun X.x(name: String) = x(name) { this }

