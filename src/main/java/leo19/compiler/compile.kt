package leo19.compiler

import leo14.Script

val Script.typed get() = emptyCompiler.plus(this).compiledTyped