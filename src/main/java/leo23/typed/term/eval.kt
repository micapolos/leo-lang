package leo23.typed.term

import leo23.term.Term
import leo23.term.eval.eval
import leo23.typed.Typed
import leo23.typed.of
import leo23.value.Value
import java.lang.reflect.Type

val Typed<Term, Type>.eval: Typed<Value, Type> get() = v.eval.of(t)