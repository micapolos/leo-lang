package leo14.typed.compiler.natives

import leo14.typed.compiler.coreString
import leo14.typed.compiler.put

val String.leoEval get() = emptyCompilerCharReader.put(this).coreString