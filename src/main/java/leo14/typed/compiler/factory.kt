package leo14.typed.compiler

import leo14.Literal
import leo14.any
import leo14.typed.Type
import leo14.typed.Typed

val lit: Literal.() -> Any = { any }

fun compiler(typed: Typed<Any>) = TypedCompiler(null, anyContext, typed)
fun compiler(type: Type) = TypeCompiler<Any>(null, type)
