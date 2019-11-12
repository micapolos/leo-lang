package leo14.typed.compiler

import leo14.any
import leo14.typed.Type
import leo14.typed.Typed

fun compiler(typed: Typed<Any>): Compiler<Any> = TypedCompiler(null, typed) { any }
fun compiler(type: Type): Compiler<Any> = TypeCompiler(null, type)
