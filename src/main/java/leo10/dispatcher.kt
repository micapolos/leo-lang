package leo10

data class Dispatcher(
	val beginEntryDict: Dict<DispatcherEntry>,
	val endEntryOrNull: DispatcherEntry?)

sealed class DispatcherEntry

data class FunctionDispatcherEntry(
	val function: Function) : DispatcherEntry()

data class DispatcherDispatcherEntry(
	val dispatcher: Dispatcher) : DispatcherEntry()

val dispatcher = Dispatcher(stringDict(), null)

val DispatcherEntry.dispatcherOrNull get() = (this as? DispatcherDispatcherEntry)?.dispatcher

fun Dispatcher.begin(name: String): DispatcherEntry? =
	beginEntryDict.at(name)

val Dispatcher.end: DispatcherEntry?
	get() =
		endEntryOrNull

fun Dispatcher.push(line: ScriptLine): DispatcherEntry? =
	begin(line.name)?.dispatcherOrNull?.push(line.script)?.dispatcherOrNull?.end

fun Dispatcher.push(link: ScriptLink): DispatcherEntry? =
	push(link.script)?.dispatcherOrNull?.push(link.line)

fun Dispatcher.push(script: Script): DispatcherEntry? =
	when (script) {
		is EmptyScript -> DispatcherDispatcherEntry(this)
		is LinkScript -> push(script.link)
		is FunctionScript -> null
	}
