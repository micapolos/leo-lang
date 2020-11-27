package leo23.term.chez

import leo16.term.chez.eval
import leo23.term.Expr

val Expr.evalString: String get() = string.eval