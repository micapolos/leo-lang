package lambda.lib

import lambda.Term
import lambda.invoke

val nil = branch0(error)
val list = branch1

val Term.link get() = branchSwitch(error, id)
