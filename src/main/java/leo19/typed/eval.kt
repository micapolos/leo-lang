package leo19.typed

import leo19.term.eval.eval
import leo19.term.eval.term

val Typed.eval: Typed get() = term.eval.term.of(type)