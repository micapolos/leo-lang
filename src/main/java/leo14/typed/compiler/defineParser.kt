package leo14.typed.compiler

import leo13.fold
import leo13.reverse
import leo14.*
import leo14.typed.Type
import leo14.typed.type

data class DefineParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val memory: Memory<T>)

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
				fold(this@parse.memory.itemStack.reverse) {
					plus(it)
				}
			}
	}

fun <T> DefineParser<T>.plus(item: MemoryItem<T>): DefineParser<T> =
	copy(memory = memory.plus(item))

fun <T> DefineParser<T>.beginIs(type: Type) =
	parentCompiledParser.begin(DefineIsParserParent(this, type))

fun <T> DefineParser<T>.beginGives(type: Type) =
	parentCompiledParser
		.begin(DefineGivesParserParent(this, type), CompilerKind.COMPILER)
		.updateCompiled { plusGiven(Keyword.GIVEN stringIn parentCompiledParser.context.language, type) }