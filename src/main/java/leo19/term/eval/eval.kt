package leo19.term.eval

import leo19.value.Scope
import leo19.value.eval
import leo19.term.Term
import leo19.term.expr.expr

val Term.eval get() = expr.eval
fun Scope.eval(term: Term) = eval(term.expr)
