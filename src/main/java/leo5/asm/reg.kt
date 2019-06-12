package leo5.asm

sealed class Reg

data class LhsReg(val lhs: Lhs) : Reg()
data class RhsReg(val rhs: Rhs) : Reg()

fun reg(lhs: Lhs): Reg = LhsReg(lhs)
fun reg(rhs: Rhs): Reg = RhsReg(rhs)
