package leo19.typed

import leo19.term.eval.eval
import leo19.term.eval.term
import leo19.term.expr.expr

val Typed.eval: Typed get() = term.expr.eval.term.of(type)