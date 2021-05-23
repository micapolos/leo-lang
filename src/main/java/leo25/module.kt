package leo25

import leo13.Stack
import leo13.push
import leo13.stack

data class Module(val definitionStack: Stack<Definition>)

fun module(definitionStack: Stack<Definition>) = Module(definitionStack)
fun module(vararg definitions: Definition) = module(stack(*definitions))
operator fun Module.plus(definition: Definition) = module(definitionStack.push(definition))
