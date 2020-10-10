package vm3.basm.asm

import leo13.toList
import vm3.asm.Op
import vm3.basm.Block

data class Compiled(
	val opList: List<Op>,
	val jumpAddress: Int
)

val Block.compiled
	get() =
		let { block ->
			emptyCompiler
				.addProcedures(block)
				.run {
					opCount.let { address ->
						add(block).add(Op.Exit).run {
							Compiled(opStack.toList(), address)
						}
					}
				}
		}
