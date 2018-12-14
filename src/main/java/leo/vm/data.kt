package leo.vm

import leo.base.*

data class Data(
	val binaryTree: BinaryTree<Variable<Bit>>)

fun newData(bit: Bit): Data =
	Data(binaryTree(newVariable(bit)))

fun newData(zeroData: Data, oneData: Data): Data =
	Data(binaryTree(zeroData.binaryTree, oneData.binaryTree))
