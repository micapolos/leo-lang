package vm2

sealed class Size
object Size32 : Size()
object Size64 : Size()

sealed class Instruction

sealed class NumericInstruction : Instruction()
data class ConstantInstruction(val numeric: Numeric) : NumericInstruction()

data class IntegerUnaryNumericInstruction(val size: Size, val op: IntegerUnaryOp) : NumericInstruction()
data class FloatingPointUnaryNumericInstruction(val size: Size, val op: FloatingPointUnaryOp) : NumericInstruction()
data class IntegerBinaryNumericInstruction(val size: Size, val op: IntegerBinaryOp) : NumericInstruction()
data class FloatingPointBinaryNumericInstruction(val size: Size, val op: FloatingPointBinaryOp) : NumericInstruction()
data class IntegerTestNumericInstruction(val size: Size, val op: IntegerTestOp) : NumericInstruction()
data class IntegerComparizonNumericInstruction(val size: Size, val op: IntegerComparisonOp) : NumericInstruction()
data class FloatingPointComparisonNumericInstruction(val size: Size, val op: FloatingPointComparisonOp) : NumericInstruction()

sealed class IntegerUnaryOp
object IntegerCountLeadingZerosOp : IntegerUnaryOp()
object IntegerCountTrailingZerosOp : IntegerUnaryOp()
object IntegerCountOnes : IntegerUnaryOp()

sealed class IntegerBinaryOp
object IntegerAddOp : IntegerBinaryOp()
object IntegerSubtractOp : IntegerBinaryOp()
object IntegerMultiplyOp : IntegerBinaryOp()
data class IntegerDivideOp(val signed: Boolean) : IntegerBinaryOp()
data class IntegerRemainderOp(val signed: Boolean) : IntegerBinaryOp()
object IntegerBitwiseAndOp : IntegerBinaryOp()
object IntegerBitwiseOrOp : IntegerBinaryOp()
object IntegerBitwiseXorOp : IntegerBinaryOp()
object IntegerShiftLeftOp : IntegerBinaryOp()
data class IntegerShiftRightOp(val signed: Boolean) : IntegerBinaryOp()
object IntegerRotateLeftOp : IntegerBinaryOp()
object IntegerRotateRightOp : IntegerBinaryOp()

sealed class IntegerTestOp
object IntegerEqualToZeroOp : IntegerTestOp()

sealed class FloatingPointUnaryOp
object FloatingPointAbsoluteValueOp : FloatingPointUnaryOp()
object FloatingPointNegateOp : FloatingPointUnaryOp()
object FloatingPointSquareRootOp : FloatingPointUnaryOp()
object FloatingPointCeilingOp : FloatingPointUnaryOp()
object FloatingPointFloorOp : FloatingPointUnaryOp()
object FloatingPointTruncateOp : FloatingPointUnaryOp()
object FloatingPointRoundToNearestIntegerOp : FloatingPointUnaryOp()

sealed class FloatingPointBinaryOp
object FloatingPointAddOp : FloatingPointBinaryOp()
object FloatingPointSubtractOp : FloatingPointBinaryOp()
object FloatingPointMultiplyOp : FloatingPointBinaryOp()
object FloatingPointDivideOp : FloatingPointBinaryOp()
object FloatingPointMinimumOp : FloatingPointBinaryOp()
object FloatingPointMaximumOp : FloatingPointBinaryOp()
object FloatingPointCopySignOp : FloatingPointBinaryOp()

sealed class IntegerComparisonOp
object IntegerEqualToOp : IntegerComparisonOp()
object IntegerNotEqualToOp : IntegerComparisonOp()
data class IntegerLessThanOp(var signed: Boolean) : IntegerComparisonOp()
data class IntegerGreaterThanOp(var signed: Boolean) : IntegerComparisonOp()
data class IntegerLessThanOrEqualToOp(var signed: Boolean) : IntegerComparisonOp()
data class IntegerGreaterThanOrEqualToOp(var signed: Boolean) : IntegerComparisonOp()

sealed class FloatingPointComparisonOp
object FloatingPointEqualToOp : FloatingPointComparisonOp()
object FloatingPointNotEqualToOp : FloatingPointComparisonOp()
object FloatingPointLessThanOp : FloatingPointComparisonOp()
object FloatingPointGreaterThanOp : FloatingPointComparisonOp()
object FloatingPointLessThanOrEqualToOp : FloatingPointComparisonOp()
object FloatingPointGreaterThanOrEqualToOp : FloatingPointComparisonOp()

sealed class ControlInstruction
data class BlockInstruction(val instructions: List<Instruction>) : ControlInstruction()