package leo21.token.body

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo21.compiled.Compiled
import leo21.token.type.compiler.Lines
import leo21.token.type.compiler.emptyLines
import leo21.token.type.compiler.plus
import leo21.type.Line

data class Module(val bindings: Bindings, val lines: Lines, val definitions: Definitions) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "module" lineTo script(
			bindings.reflectScriptLine,
			lines.reflectScriptLine,
			definitions.anyReflectScriptLine)
}

val emptyModule = Module(emptyBindings, emptyLines, emptyDefinitions)

fun Module.plus(definition: Definition): Module =
	Module(
		bindings.plus(definition.binding),
		lines,
		definitions.plus(definition))

fun Module.plus(definitions: Definitions): Module =
	fold(definitions.definitionStack.reverse) { plus(it) }

fun Module.plus(lines: Lines): Module =
	fold(lines.lineStack.reverse) { plus(it) }

fun Module.plus(module: Module): Module =
	plus(lines).plus(module.definitions)

fun Module.plus(line: Line): Module =
	copy(lines = lines.plus(line))

fun Module.resolveOrNull(compiled: Compiled): Compiled? =
	bindings.resolveOrNull(compiled)

fun Module.begin(given: Given): Module =
	Module(bindings.plus(given.binding), lines, emptyDefinitions)

fun Compiled.wrap(module: Module): Compiled =
	wrap(module.definitions)

val Module.printScript: Script
	get() =
		lines.printScript.plus(definitions.printScript)

val Module.begin: Module
	get() =
		Module(bindings, lines, emptyDefinitions)