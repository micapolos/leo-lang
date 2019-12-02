package leo14.typed.compiler.natives

import leo14.defaultLanguage
import leo14.native.Native
import leo14.typed.Type
import leo14.typed.compiler.*

fun compiler(type: Type): Compiler<Native> =
	compiler(TypeParser(null, null, defaultLanguage, nativeTypeContext, type))

fun CompilerKind.compiler(compiled: Compiled<Native>): Compiler<Native> =
	compiler(CompiledParser(null, this, context, compiled))

fun compiler(compiled: Compiled<Native>): Compiler<Native> =
	CompilerKind.COMPILER.compiler(compiled)

fun evaluator(compiled: Compiled<Native>): Compiler<Native> =
	CompilerKind.EVALUATOR.compiler(compiled)

val emptyCompiler: Compiler<Native> = compiler(compiled())
val emptyEvaluator: Compiler<Native> = evaluator(compiled())