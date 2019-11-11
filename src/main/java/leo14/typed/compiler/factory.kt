package leo14.typed.compiler

import leo14.typed.Type
import leo14.typed.Typed

fun <T> compiler(typed: Typed<T>): Compiler<T> = TypedCompiler(null, typed)
fun <T> compiler(type: Type): Compiler<T> = TypeCompiler(null, type)
