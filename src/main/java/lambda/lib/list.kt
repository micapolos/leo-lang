package lambda.lib

import lambda.Term
import lambda.invoke

val emptyList = branch0(error)
val linkList = branch1

val Term.listForEmptyOrLink get() = branchSwitch
val Term.listLink get() = branchSwitch(error, id)
