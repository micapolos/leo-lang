package leo13.type

import leo13.Stack

data class TypedSwitch(val caseTypedStack: Stack<CaseTyped>)

val Stack<CaseTyped>.typedSwitch: TypedSwitch get() = TypedSwitch(this)