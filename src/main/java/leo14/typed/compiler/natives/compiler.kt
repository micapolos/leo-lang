package leo14.typed.compiler.natives

import leo14.englishDictionary
import leo14.native.Native
import leo14.typed.Type
import leo14.typed.compiler.*
import leo14.typed.typed

fun compiler(type: Type): Compiler<Native> =
	compiler(TypeParser(null, null, englishDictionary, nativeTypeContext, type))

fun compiler(compiled: Compiled<Native>, phase: Phase = Phase.COMPILER): Compiler<Native> =
	compiler(CompiledParser(null, context, phase, compiled))

val emptyCompiler: Compiler<Native> = compiler(compiled(typed()), Phase.EVALUATOR)