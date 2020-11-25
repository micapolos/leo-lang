package leo23.term.chez

import leo16.term.chez.eval
import leo23.term.Term

val Term.evalString: String get() = string.eval