package leo23.typed.term

import leo23.term.eval.eval
import leo23.typed.of
import leo23.typed.value.Evaluated

val Compiled.eval: Evaluated get() = v.eval.of(t)
