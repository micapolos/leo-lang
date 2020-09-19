package vm3

val x00_returnOpcode = 0x00
val x01_nopOpcode = 0x01
val x02_syscallOpcode = 0x02

val x03_jumpOpcode = 0x03
val x04_jumpIfOpcode = 0x04
val x05_callOpcode = 0x05

val x08_setConst32Opcode = 0x08
val x09_set32Opcode = 0x09
val x0A_setOffsetOpcode = 0x0A
val x0B_setIndexedOpcode = 0x0B

val x10_i32IncOpcode = 0x10
val x11_i32DecOpcode = 0x11
val x12_i32UnaryMinusOpcode = 0x12
val x13_i32CountLeadingZeroBitsOpcode = 0x13
val x14_i32CountTrailingZeroBitsOpcode = 0x14
val x15_bitCountOpcode = 0x15
val x16_i32PlusOpcode = 0x16
val x17_i32MinusOpcode = 0x17
val x18_i32TimesOpcode = 0x18
val x19_i32DivOpcode = 0x19

val x1B_i32InvOpcode = 0x1B
val x1D_i32AndOpcode = 0x1D
val x1E_i32OrOpcode = 0x1E
val x1F_i32XorOpcode = 0x1F
val x20_i32ShlOpcode = 0x20
val x21_i32ShrOpcode = 0x21
val x22_i32UshrOpcode = 0x22
val x23_i32RolOpcode = 0x23
val x24_i32RorOpcode = 0x24

val x28_i32IsZeroOpcode = 0x28
val x29_i32EqOpcode = 0x29
val x2A_i32NeqOpcode = 0x2A
val x2B_i32LtOpcode = 0x2B
val x2C_i32GtOpcode = 0x2C
val x2D_i32LeOpcode = 0x2D
val x2E_i32GeOpcode = 0x2E

val x33_f32PlusOpcode = 0x33
val x34_f32MinusOpcode = 0x34
val x35_f32TimesOpcode = 0x35
val x36_f32DivOpcode = 0x36
val x37_f32RemOpcode = 0x37

val x40_f32IsZeroOpcode = 0x40
val x41_f32EqOpcode = 0x41
val x42_f32NeqOpcode = 0x42
val x43_f32LtOpcode = 0x43
val x44_f32GtOpcode = 0x44
val x45_f32LeOpcode = 0x45
val x46_f32GeOpcode = 0x46
