package leo21.compiled

import leo14.lambda.Term
import leo21.prim.Prim
import leo21.type.Recursive

data class RecursiveCompiled(val term: Term<Prim>, val recursive: Recursive)

infix fun Term<Prim>.of(recursive: Recursive) = RecursiveCompiled(this, recursive)
fun recursive(compiled: Compiled) = compiled.term of leo21.type.recursive(compiled.type)
