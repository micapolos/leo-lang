package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orIfNull
import leo14.*
import leo14.native.Native
import leo14.parser.*
import leo14.syntax.*

data class CharCompiler(
	val tokenParser: SpacedTokenParser,
	val compiler: Compiler<Native>)

val emptyCharCompiler = CharCompiler(newSpacedTokenParser, EMPTY_COMPILER)

fun CharCompiler.put(char: Char): CharCompiler =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (parsedTokenParser.canContinue) CharCompiler(parsedTokenParser, compiler)
					else CharCompiler(newSpacedTokenParser, compiler.parse(token))
				}
				?: CharCompiler(parsedTokenParser, compiler)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharCompiler(newSpacedTokenParser, compiler.parse(token)).put(char) }
				?: error("$this.put($char)")
		}

fun CharCompiler.put(string: String) =
	fold(string) { put(it) }

fun CharCompiler.put(script: Script) =
	copy(compiler = compiler.parse(script))

val CharCompiler.spacedString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(compiler)
		} + tokenParser.spacedString

val CharCompiler.coreString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(compiler)
		} + tokenParser.coreString

val CharCompiler.coreColorString: String
	get() =
		processorString {
			map(Syntax::coreColorString).process(compiler)
		} + tokenParser.coreString

val CharCompiler.indentColorString: String
	get() =
		emptyFragment
			.foldProcessor<Fragment, Syntax>({ plus(it.token).notNullOrError("$this.write($it)") }) {
				process(compiler)
			}.indentString + tokenParser.coreString

val String.leoEval get() = emptyCharCompiler.put(this).coreString