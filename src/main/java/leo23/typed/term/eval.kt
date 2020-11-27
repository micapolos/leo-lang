package leo23.typed.term

import leo23.term.Expr
import leo23.term.eval.eval
import leo23.typed.Typed
import leo23.typed.of
import leo23.value.Value
import java.lang.reflect.Type

val Typed<Expr, Type>.eval: Typed<Value, Type> get() = v.eval.of(t)
