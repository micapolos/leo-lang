package leo13.script

import leo13.Script
import leo9.Stack
import leo9.push
import leo9.stack

data class Bindings(val stack: Stack<Script>)

val Stack<Script>.bindings get() = Bindings(this)
fun Bindings.push(script: Script) = stack.push(script).bindings
fun bindings(vararg scripts: Script) = stack(*scripts).bindings
