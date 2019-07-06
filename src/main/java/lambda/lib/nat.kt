package lambda.lib

import lambda.Term
import lambda.invoke

val zeroNat get() = branch0(void)
val Term.natInc get() = branch1(this)

val Term.natForVoidOrDec get() = branchSwitch
val Term.natDecOrZero get() = natForVoidOrDec(const(zeroNat), id)
