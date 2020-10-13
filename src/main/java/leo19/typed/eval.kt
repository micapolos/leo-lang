package leo19.typed

import leo19.value.eval
import leo19.value.term
import leo19.term.expr.expr

val Typed.eval: Typed get() = term.expr.eval.term.of(type)