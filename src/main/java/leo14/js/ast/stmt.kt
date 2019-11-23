package leo14.js.ast

sealed class Stmt

data class SetStmt(val set: Set) : Stmt()
data class SetNamedStmt(val setNamed: SetNamed) : Stmt()
data class RetStmt(val ret: Ret) : Stmt()
data class ExprStmt(val expr: Expr) : Stmt()
data class BlockStmt(val block: Block) : Stmt()

fun stmt(set: Set): Stmt = SetStmt(set)
fun stmt(setNamed: SetNamed): Stmt = SetNamedStmt(setNamed)
fun stmt(ret: Ret): Stmt = RetStmt(ret)
fun stmt(expr: Expr): Stmt = ExprStmt(expr)
fun stmt(block: Block): Stmt = BlockStmt(block)

val Stmt.code: String
	get() =
		when (this) {
			is SetStmt -> set.stmtCode
			is SetNamedStmt -> setNamed.stmtCode
			is RetStmt -> ret.stmtCode
			is ExprStmt -> expr.stmtCode
			is BlockStmt -> block.stmtCode
		}
