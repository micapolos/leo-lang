package leo21.token.body

import leo15.dsl.*
import leo21.compiled.Compiled
import leo21.token.processor.bodyCompiler
import leo21.token.processor.process
import leo21.token.processor.processor

fun body(f: F): Body =
	emptyBodyCompiler.processor.process(f).bodyCompiler.rootBody

fun compiled(f: F): Compiled =
	body(f).wrapCompiled
