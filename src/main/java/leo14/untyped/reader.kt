package leo14.untyped

import leo14.*

sealed class Reader
data class QuotedReader(val quoted: Quoted) : Reader()
data class UnquotedReader(val unquoted: Unquoted) : Reader()
data class CodeReader(val code: Code) : Reader()

data class Quoted(
	val opOrNull: QuotedOp?,
	val context: Context,
	val depth: Int,
	val program: Program)

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
data class UnquotedApplyCodeOp(val unquoted: Unquoted, val begin: Begin) : CodeOp()

fun Reader.write(token: Token): Reader? =
	when (this) {
		is QuotedReader -> write(token)
		is UnquotedReader -> write(token)
		is CodeReader -> write(token)
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
		begin.string == "quote" ->
			QuotedReader(
				Quoted(
					QuotedAppendQuotedOp(this, begin),
					context,
					depth.inc(),
					program()))
		begin.string == "unquote" && depth > 0 ->
			when (depth) {
				1 ->
					UnquotedReader(
						Unquoted(
							QuotedPlusUnquotedOp(this),
							context.resolver(program())))
				else -> QuotedReader(
					Quoted(
						QuotedAppendQuotedOp(this, begin),
						context,
						depth.dec(),
						program()))
			}
		else ->
			QuotedReader(
				Quoted(
					QuotedAppendQuotedOp(this, begin),
					context,
					depth,
					program()))
	}

fun Unquoted.write(begin: Begin): Reader =
	when (begin.string) {
		"quote" ->
			QuotedReader(
				Quoted(
					UnquotedPlusQuotedOp(this),
					resolver.context,
					1,
					program()))
		"function", "does" ->
			CodeReader(
				Code(
					UnquotedApplyCodeOp(this, begin),
					script()))
		else ->
			UnquotedReader(
				Unquoted(
					UnquotedResolveUnquotedOp(this, begin),
					resolver))
	}

fun Code.write(begin: Begin): Reader =
	CodeReader(
		Code(
			CodeAppendCodeOp(this, begin),
			script()))

fun Quoted.write(end: End): Reader? =
	opOrNull?.write(program)

fun QuotedOp.write(program: Program): Reader? =
	when (this) {
		is QuotedAppendQuotedOp ->
			QuotedReader(
				quoted.copy(
					program = program.plus(begin.string valueTo program)))
		is UnquotedPlusQuotedOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.append(program)))
	}

fun Unquoted.write(end: End): Reader? =
	opOrNull?.write(resolver.program)

fun UnquotedOp.write(program: Program): Reader? =
	when (this) {
		is UnquotedResolveUnquotedOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.apply(begin.string valueTo program)))
		is QuotedPlusUnquotedOp ->
			QuotedReader(
				quoted.copy(
					program = quoted.program.plus(program)))
	}

fun Code.write(end: End): Reader? =
	opOrNull?.write(script)

fun CodeOp.write(script: Script): Reader? =
	when (this) {
		is CodeAppendCodeOp ->
			CodeReader(
				code.copy(
					script = code.script.plus(begin.string lineTo script)))
		is UnquotedApplyCodeOp ->
			UnquotedReader(
				unquoted.copy(
					resolver = unquoted.resolver.apply(begin.string valueTo script.program)))
	}

fun Quoted.write(literal: Literal): Reader? =
	QuotedReader(copy(program = program.plus(value(literal))))

fun Unquoted.write(literal: Literal): Reader? =
	UnquotedReader(copy(resolver = resolver.apply(value(literal))))

fun Code.write(literal: Literal): Reader? =
	CodeReader(copy(script = script.plus(line(literal))))
