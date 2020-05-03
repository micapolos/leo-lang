package leo16

import leo14.Script
import leo14.emptyScript

data class Compiled(val script: Script)

val Script.compiled get() = Compiled(this)
val emptyCompiled = emptyScript.compiled