package leo.lab.v2

sealed class Jump

data class SiblingJump(
	val sibling: Sibling) : Jump()

data class ParentJump(
	val parent: Parent) : Jump()

val Sibling.jump: Jump
	get() =
		SiblingJump(this)

val Parent.jump: Jump
	get() =
		ParentJump(this)

