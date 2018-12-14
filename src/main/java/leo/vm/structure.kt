package leo.vm

import leo.base.*

data class Structure(
	val unitBinaryTree: BinaryTree<Unit>)

val bitStructure = Structure(binaryTree(Unit))

fun structure(zeroStructure: Structure, oneStructure: Structure): Structure =
	Structure(binaryTree(zeroStructure.unitBinaryTree, oneStructure.unitBinaryTree))

fun Structure.newDataWith(bit: Bit): Data =
	Data(unitBinaryTree.map { newVariable(bit) })

fun Structure.isValid(data: Data): Boolean =
	unitBinaryTree.isLike(data.binaryTree)
