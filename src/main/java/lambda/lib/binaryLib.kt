package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term
import lambda.termLet

data class BinaryLib(
	val bitZero: Term,
	val bitOne: Term,
	val pair: Term,
	val pairAt: Term,
	val branchZero: Term,
	val branchOne: Term,
	val switch: Term)

fun binaryLib(fn: (BinaryLib) -> Term) =
	term { x -> term { y -> x } }.termLet { zero ->
		term { x -> term { y -> y } }.termLet { one ->
			term { at0 -> term { at1 -> term { bit -> bit(at0, at1) } } }.termLet { pair ->
				term { pair -> term { at -> pair(at) } }.termLet { pairAt ->
					pair(one, zero).termLet { two ->
						pair(one, one).termLet { three ->
							term { at0 -> term { if0 -> term { if1 -> if0(at0) } } }.termLet { branchZero ->
								term { at1 -> term { if0 -> term { if1 -> if1(at1) } } }.termLet { branchOne ->
									term { branch -> term { if0 -> term { if1 -> branch(if0, if1) } } }.termLet { switch ->
										fn(BinaryLib(zero, one, pair, pairAt, branchZero, branchOne, switch))
									}
								}
							}
						}
					}
				}
			}
		}
	}