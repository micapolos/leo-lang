package leo13.compiler

import leo13.type.Type
import leo13.value.Switch

data class SwitchCompiled(val switch: Switch, val type: Type)

fun compiled(switch: Switch, type: Type) = SwitchCompiled(switch, type)
