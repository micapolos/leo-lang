package leo14.typed.compiler.natives

import leo14.coreString
import leo14.put

val String.leoEval get() = emptyCompilerCharReader.put(this).coreString