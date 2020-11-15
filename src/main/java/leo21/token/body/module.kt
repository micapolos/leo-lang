package leo21.token.body

import leo.base.updateIfNotNull
import leo13.fold
import leo13.reverse
import leo21.compiled.Compiled

data class Module(val bindings: Bindings, val definitions: Definitions)

fun Bindings.module(definitions: Definitions) = Module(this, definitions)
val emptyModule = emptyBindings.module(emptyDefinitions)

fun Module.plus(definition: Definition): Module =
	Module(
		bindings.updateIfNotNull(definition.bindingOrNull) { plus(it) },
		definitions.plus(definition))

fun Module.plus(definitions: Definitions): Module =
	fold(definitions.definitionStack.reverse) { plus(it) }

fun Module.resolveOrNull(compiled: Compiled): Compiled? =
	bindings.resolveOrNull(compiled)

fun Module.begin(given: Given): Module =
	Module(bindings.plus(given.binding), emptyDefinitions)

fun Compiled.wrap(module: Module): Compiled =
	wrap(module.definitions)