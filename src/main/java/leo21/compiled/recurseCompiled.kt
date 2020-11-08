package leo21.compiled

import leo14.lambda.Term
import leo21.prim.Prim
import leo21.type.Recurse

data class RecurseCompiled(val term: Term<Prim>, val recursive: Recurse)

infix fun Term<Prim>.of(recurse: Recurse) = RecurseCompiled(this, recurse)
