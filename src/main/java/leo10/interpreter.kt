package leo10

import leo.base.fail
import leo.base.failIfOr

data class Interpreter(
	val parentOrNull: InterpreterParent?,
	val dispatcher: Dispatcher,
	val localDispatcherOrNull: Dispatcher?,
	val script: Script)

data class InterpreterParent(
	val interpreter: Interpreter,
	val name: String)

val Script.eval
	get() =
		interpreter.push(this).rootScript

val interpreter =
	Interpreter(null, dispatcher, null, script())

val Interpreter.rootScript get() = failIfOr(parentOrNull != null) { script }

fun Interpreter.push(token: Token) =
	when (token) {
		is BeginToken -> push(token.begin)
		is EndToken -> push(token.end)
	}

fun Interpreter.push(begin: StringBegin) =
	begin(begin.string)

fun Interpreter.push(end: End) =
	this.end

fun Interpreter.begin(name: String) =
	Interpreter(
		InterpreterParent(this, name),
		dispatcher,
		dispatcher,
		script())

val Interpreter.end
	get() =
		parentOrNull!!.endWith(script)

fun Interpreter.push(script: Script): Interpreter =
	when (script) {
		is EmptyScript -> this
		is LinkScript -> push(script.link)
		is FunctionScript -> fail()
	}

fun Interpreter.push(link: ScriptLink): Interpreter =
	push(link.script).push(link.line)

fun Interpreter.push(line: ScriptLine): Interpreter =
	begin(line.name).push(line.script).end

fun InterpreterParent.endWith(script: Script) =
	interpreter.endWith(name lineTo script)

fun Interpreter.endWith(line: ScriptLine): Interpreter =
	when (line.script) {
		is EmptyScript -> maybeEndWithGet(line.name) ?: dispatch(line)
		is LinkScript -> dispatch(line)
		is FunctionScript -> dispatch(line)
	}

fun Interpreter.maybeEndWithGet(name: String) =
	get(name)?.let { replace(it) }

fun Interpreter.dispatch(line: ScriptLine): Interpreter =
	script.plus(line).let { script ->
		localDispatcherOrNull
			?.push(line)
			?.let { entry ->
				when (entry) {
					is FunctionDispatcherEntry ->
						replace(entry.function.call(script))
					is DispatcherDispatcherEntry ->
						Interpreter(
							parentOrNull,
							dispatcher,
							entry.dispatcher,
							script)
				}
			}
			?: Interpreter(
				parentOrNull,
				dispatcher,
				null,
				script)
	}

fun Interpreter.replace(script: Script): Interpreter =
	Interpreter(
		parentOrNull,
		dispatcher,
		dispatcher.push(script)?.dispatcherOrNull,
		script)

fun Interpreter.get(name: String): Script? =
	script[name] ?: parentOrNull?.get(name)

fun InterpreterParent.get(name: String): Script? =
	interpreter.get(name)
