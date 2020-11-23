package leo21.token.processor

import leo.base.fold
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo14.matching.name
import leo21.compiled.Compiled
import leo21.compiled.FieldCompiled
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.fieldTo
import leo21.compiled.lineCompiled
import leo21.compiled.plus
import leo21.token.body.wrapCompiled

val Script.compiled: Compiled
	get() =
		(emptyBodyProcessor.plus(this) as BodyCompilerProcessor).bodyCompiler.body.wrapCompiled

val Script.staticCompiled: Compiled
	get() =
		compiled().fold(lineSeq.reverse) { plus(it.staticCompiled) }

val ScriptLine.staticCompiled: LineCompiled
	get() =
		when (this) {
			is LiteralScriptLine -> lineCompiled(literal)
			is FieldScriptLine -> field.staticCompiled.lineCompiled
		}

val ScriptField.staticCompiled: FieldCompiled
	get() =
		name fieldTo rhs.staticCompiled