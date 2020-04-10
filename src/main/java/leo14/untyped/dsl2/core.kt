package leo14.untyped.dsl2

import leo.base.Parameter
import leo.base.parameter
import leo.base.update
import leo14.Reducer
import leo14.Token
import leo14.reduce
import leo14.unitReducer
import leo14.untyped.emptyReader
import leo14.untyped.write

val readerParameter = parameter(emptyReader)

val tokenReducerParameter: Parameter<Reducer<*, Token>> = parameter(unitReducer())

object X
typealias F = X.() -> X

fun X.x(token: Token) = this
	.also { readerParameter.update { it.write(token)!! } }
	.also { tokenReducerParameter.update { it.reduce(token) } }

fun X.x(name: String, f: F) = x(leo14.token(leo14.begin(name))).f().x(leo14.token(leo14.end))
fun X.x(name: String) = x(name) { this }

