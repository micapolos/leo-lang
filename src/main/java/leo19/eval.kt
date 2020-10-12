package leo19

import leo14.Script
import leo19.compiler.typed
import leo19.typed.eval
import leo19.typed.script

val Script.eval get() = typed.eval.script