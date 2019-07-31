package leo10

data class Interpreter(
	val parentOrNull: InterpreterParent?,
	val dispatcher: Dispatcher,
	val localDispatcherOrNull: Dispatcher?,
	val script: Script)

data class InterpreterParent(
	val interpreter: Interpreter,
	val name: String)

fun Interpreter.begin(name: String) =
	Interpreter(
		InterpreterParent(this, name),
		dispatcher,
		dispatcher,
		script())

val Interpreter.end
	get() =
		parentOrNull!!.endWith(script)

fun InterpreterParent.endWith(script: Script) =
	interpreter.endWith(name lineTo script)

fun Interpreter.endWith(line: ScriptLine): Interpreter =
	when (line.script) {
		is EmptyScript -> maybeEndWithGet(line.name) ?: dispatch(line)
		is LinkScript -> dispatch(line)
	}

fun Interpreter.maybeEndWithGet(name: String) =
	scriptAtOrNull(name)?.let { replace(it) }

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

fun Interpreter.scriptAtOrNull(name: String): Script? =
	script.atOrNull(name) ?: parentOrNull?.scriptAtOrNull(name)

fun InterpreterParent.scriptAtOrNull(name: String): Script? =
	interpreter.scriptAtOrNull(name)