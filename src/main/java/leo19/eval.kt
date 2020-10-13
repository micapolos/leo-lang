package leo19

import leo14.Script
import leo19.compiler.typed
import leo19.decompiler.script
import leo19.decompiler.typedScript
import leo19.typed.eval

val Script.eval: Script get() = typed.eval.script
val Script.typedEval: Script get() = typed.eval.typedScript