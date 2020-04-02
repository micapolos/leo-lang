package leo14.typed.compiler

import leo13.Index
import leo14.*
import leo14.typed.Type
import leo14.typed.type

data class DefineParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val memory: Memory<T>,
	val localSizeIndex: Index)

fun <T> DefineParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken ->
			compiler(
				TypeParser(
					null,
					DefineTypeBeginner(this),
					parentCompiledParser.context.language,
					parentCompiledParser.context.typeContext,
					type())).parse(token)
		is BeginToken ->
			compiler(
				TypeParser(
					null,
					DefineTypeBeginner(this),
					parentCompiledParser.context.language,
					parentCompiledParser.context.typeContext,
					type())).parse(token)
		is EndToken ->
			parentCompiledParser.nextCompiler {
				updateMemory { this@parse.memory }.updateLocalIndex { plus(this@parse.localSizeIndex) }
			}
	}

fun <T> DefineParser<T>.plus(item: MemoryItem<T>): DefineParser<T> =
	copy(memory = memory.plus(item), localSizeIndex = localSizeIndex.inc())

fun <T> DefineParser<T>.beginIs(type: Type) =
	parentCompiledParser.begin(DefineIsParserParent(this, type))

fun <T> DefineParser<T>.beginGives(type: Type) =
	parentCompiledParser
		.updateCompiled { updateMemory { this@beginGives.memory } }
		.begin(DefineGivesParserParent(this, type), CompilerKind.COMPILER)
		.updateCompiled { plusGiven(Keyword.GIVEN stringIn parentCompiledParser.context.language, type) }