package leo14.untyped

import leo14.*

sealed class Reader
data class QuotedReader(val quoted: Quoted) : Reader()
data class UnquotedReader(val unquoted: Unquoted) : Reader()
data class CodeReader(val code: Code) : Reader()

data class Quoted(
	val opOrNull: QuotedOp?,
	val compiler: Compiler,
	val depth: Int,
	val thunk: Thunk)

sealed class QuotedOp
data class QuotedAppendQuotedOp(val quoted: Quoted, val begin: Begin) : QuotedOp()
data class UnquotedPlusQuotedOp(val unquoted: Unquoted) : QuotedOp()

data class Unquoted(
	val opOrNull: UnquotedOp?,
	val resolver: Resolver)

sealed class UnquotedOp
data class UnquotedResolveUnquotedOp(val unquoted: Unquoted, val begin: Begin) : UnquotedOp()
data class QuotedPlusUnquotedOp(val quoted: Quoted) : UnquotedOp()

data class Code(val opOrNull: CodeOp?, val script: Script)

sealed class CodeOp
data class CodeAppendCodeOp(val code: Code, val begin: Begin) : CodeOp()
data class UnquotedAssertCodeOp(val unquoted: Unquoted) : CodeOp()
data class UnquotedFunctionCodeOp(val unquoted: Unquoted) : CodeOp()
data class UnquotedGivesCodeOp(val unquoted: Unquoted) : CodeOp()
data class UnquotedGetCodeOp(val unquoted: Unquoted) : CodeOp()
data class UnquotedLazyCodeOp(val unquoted: Unquoted) : CodeOp()

val emptyReader: Reader
	get() =
		UnquotedReader(Unquoted(null, resolver(program())))

val Resolver.reader: Reader
	get() =
		UnquotedReader(Unquoted(null, this))

fun Reader.write(token: Token): Reader? =
	when (this) {
		is QuotedReader -> quoted.write(token)
		is UnquotedReader -> unquoted.write(token)
		is CodeReader -> code.write(token)
	}

fun Quoted.write(token: Token): Reader? =
	when (token) {
		is LiteralToken -> write(token.literal)
		is BeginToken -> write(token.begin)
		is EndToken -> write(token.end)
	}

fun Unquoted.write(token: Token): Reader? =
	when (token) {
		is LiteralToken -> write(token.literal)
		is BeginToken -> write(token.begin)
		is EndToken -> write(token.end)
	}

fun Code.write(token: Token): Reader? =
	when (token) {
		is LiteralToken -> write(token.literal)
		is BeginToken -> write(token.begin)
		is EndToken -> write(token.end)
	}

fun Quoted.write(begin: Begin): Reader =
	when {
		begin.string == quoteName ->
			QuotedReader(
				Quoted(
					QuotedAppendQuotedOp(this, begin),
					compiler,
					depth.inc(),
					thunk(program())))
		begin.string == unquoteName && depth > 0 ->
			when (depth) {
				1 ->
					UnquotedReader(
						Unquoted(
							QuotedPlusUnquotedOp(this),
							compiler.resolver(program())))
				else -> QuotedReader(
					Quoted(
						QuotedAppendQuotedOp(this, begin),
						compiler,
						depth.dec(),
						thunk(program())))
			}
		else ->
			QuotedReader(
				Quoted(
					QuotedAppendQuotedOp(this, begin),
					compiler,
					depth,
					thunk(program())))
	}

fun Unquoted.write(begin: Begin): Reader =
	when (begin.string) {
		assertName ->
			CodeReader(
				Code(
					UnquotedAssertCodeOp(this),
					script()))
		quoteName ->
			QuotedReader(
				Quoted(
					UnquotedPlusQuotedOp(this),
					resolver.compiler,
					1,
					thunk(program())))
		functionName ->
			CodeReader(
				Code(
					UnquotedFunctionCodeOp(this),
					script()))
		doesName ->
			CodeReader(
				Code(
					UnquotedGivesCodeOp(this),
					script()))
		getName ->
			CodeReader(
				Code(
					UnquotedGetCodeOp(this),
					script()))
		lazyName ->
			if (resolver.program.isEmpty)
				CodeReader(
					Code(
						UnquotedLazyCodeOp(this),
						script()))
			else
				UnquotedReader(
					Unquoted(
						UnquotedResolveUnquotedOp(this, begin),
						resolver.compiler.resolver(program())))
		else ->
			UnquotedReader(
				Unquoted(
					UnquotedResolveUnquotedOp(this, begin),
					resolver.compiler.resolver(program())))
	}

fun Code.write(begin: Begin): Reader =
	CodeReader(
		Code(
			CodeAppendCodeOp(this, begin),
			script()))

fun Quoted.write(end: End): Reader? =
	opOrNull?.write(thunk)

fun QuotedOp.write(thunk: Thunk): Reader? =
	when (this) {
		is QuotedAppendQuotedOp ->
			QuotedReader(
				quoted.copy(
					thunk = quoted.thunk.plus(begin.string lineTo thunk)))
		is UnquotedPlusQuotedOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.append(thunk)))
	}

fun Unquoted.write(end: End): Reader? =
	opOrNull?.write(resolver.thunk)

fun UnquotedOp.write(thunk: Thunk): Reader? =
	when (this) {
		is UnquotedResolveUnquotedOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.apply(begin.string lineTo thunk)))
		is QuotedPlusUnquotedOp ->
			QuotedReader(
				quoted.copy(
					// TODO: IS it correct?
					thunk = thunk(quoted.thunk.program.plus(thunk.program))))
	}

fun Code.write(end: End): Reader? =
	opOrNull?.write(script)

fun CodeOp.write(script: Script): Reader? =
	when (this) {
		is CodeAppendCodeOp ->
			CodeReader(
				code.copy(
					script = code.script.plus(begin.string lineTo script)))
		is UnquotedAssertCodeOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.assert(script)))
		is UnquotedFunctionCodeOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.function(script)))
		is UnquotedGivesCodeOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.does(script)))
		is UnquotedGetCodeOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.apply(getName lineTo script.program)))
		is UnquotedLazyCodeOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.lazy(script)))
	}

fun Quoted.write(literal: Literal): Reader? =
	QuotedReader(copy(thunk = thunk.plus(line(literal))))

fun Unquoted.write(literal: Literal): Reader? =
	UnquotedReader(copy(resolver = resolver.apply(line(literal))))

fun Code.write(literal: Literal): Reader? =
	CodeReader(copy(script = script.plus(leo14.line(literal))))

val Reader.program
	get() =
		(this as UnquotedReader).unquoted.resolver.program