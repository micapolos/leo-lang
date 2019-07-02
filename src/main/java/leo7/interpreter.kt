package leo7

data class Interpreter(
	val quotableFunction: Quotable<Function>,
	val parentOrNull: InterpreterParent?,
	val script: Script)

data class InterpreterParent(
	val interpreter: Interpreter,
	val begin: WordBegin)

val emptyInterpreter
	get() =
		Interpreter(identityFunction.raw.quotable, null, script())

operator fun Interpreter.plus(token: Token): Interpreter? =
	when (token) {
		is BeginToken -> plus(token.begin)
		is EndToken -> plus(token.end)
	}

operator fun Interpreter.plus(begin: WordBegin): Interpreter? =
	when (begin) {
		quoteWord.begin -> copy(quotableFunction = quotableFunction.quote)
		unquoteWord.begin -> quotableFunction.unquote?.let { quotable -> copy(quotableFunction = quotable) }
		else -> plusRaw(begin)
	}

fun Interpreter.plusRaw(begin: WordBegin): Interpreter? = TODO()

operator fun Interpreter.plus(end: End): Interpreter? =
	parentOrNull?.let { parent -> parent.interpreter.plus(parent.begin.word lineTo script) }

fun Interpreter.plus(line: Line): Interpreter? =
	when (quotableFunction) {
		is RawQuotable -> TODO()//plusMetable(quotableFunction.raw(), line)
		is QuotedQuotable -> plusQuoted(line)
	}

fun Interpreter.plusMetable(metableFunction: Metable<Function>, line: Line): Interpreter? =
	when (metableFunction) {
		is RawMetable -> plusRaw(metableFunction.raw(), line)
		is MetaMetable -> TODO()
	}

fun Interpreter.plusRaw(function: Function, line: Line): Interpreter? =
	when (line.word) {
		isWord -> TODO()
		metaWord -> TODO()//copy(quotableFunction = function.meta.metable.raw.quotable)
		else -> copy(script = function.apply(script.plus(line)))
	}

fun Interpreter.plusQuoted(line: Line) =
	copy(script = script.plus(line))

fun Interpreter.resolve(line: Line) =
	plusQuoted(line) // TODO
