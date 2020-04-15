package leo15.dsl

import leo.base.Parameter
import leo.base.parameter
import leo.base.update
import leo14.*

object X
typealias F = X.() -> X

val tokenReducerParameter: Parameter<Reducer<*, Token>> = parameter(unitReducer())

fun X.x(token: Token) = this.also { tokenReducerParameter.update { it.reduce(token) } }

fun X.x(name: String, f: F) = x(token(begin(name))).f().x(token(leo14.end))
fun X.x(name: String) = x(name) { this }

