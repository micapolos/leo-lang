package leo14.typed.compiler.natives

import leo14.defaultLanguage
import leo14.native.Native
import leo14.typed.Type
import leo14.typed.compiler.*
import leo14.typed.typed

fun compiler(type: Type): Compiler<Native> =
	compiler(TypeParser(null, null, defaultLanguage, nativeTypeContext, type))

fun compiler(compiled: Compiled<Native>): Compiler<Native> =
	compiler(CompiledParser(null, null, context, compiled))

val emptyCompiler: Compiler<Native> = compiler(compiled(typed()))