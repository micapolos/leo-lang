package lambda.lib

import lambda.Term
import lambda.ap2
import lambda.invoke
import lambda.term

val branch0 = term { value -> term { for0 -> term { for0(value) } } }
val branch1 = term { value -> term { term { for1 -> for1(value) } } }

val Term.branchSwitch get() = this
val branchSwitch get() = ap2 { branchSwitch }
