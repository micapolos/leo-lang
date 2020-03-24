package leo14.untyped.dsl2

import leo14.Token
import leo14.untyped.emptyReader
import leo14.untyped.write

var reader_ = emptyReader

object X
typealias F = X.() -> X

fun X.x(token: Token) = also { reader_ = reader_.write(token)!! }
fun X.x(name: String, f: F) = x(leo14.token(leo14.begin(name))).f().x(leo14.token(leo14.end))
fun X.x(name: String) = x(name) { this }

