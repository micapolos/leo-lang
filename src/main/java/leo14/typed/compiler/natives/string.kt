package leo14.typed.compiler.natives

import leo14.reader.coreString
import leo14.reader.put

val String.leoEval get() = emptyCompilerCharReader.put(this).coreString