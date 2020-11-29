package leo23.processor

import leo.base.fold
import leo14.Script
import leo14.tokenSeq
import leo23.type.Types

val Script.compileTypes: Types
	get() =
		(emptyProcessor.fold(tokenSeq) { plus(it) } as CompilerProcessor).node.compiler.stackCompiled.t