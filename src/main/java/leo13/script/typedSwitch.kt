package leo13.script

import leo9.Stack

data class TypedSwitch(val caseTypedStack: Stack<CaseTyped>)

val Stack<CaseTyped>.typedSwitch: TypedSwitch get() = TypedSwitch(this)