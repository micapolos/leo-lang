package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term
import leo.binary.bit0
import leo.binary.isZero

val i2 = pair
val i4 = pair
val i8 = pair
val i16 = pair
val i32 = pair

val Int.i1Term get() = if (bit0.isZero) zeroBit else oneBit
val Int.i2Term get() = i2(ushr(1).i1Term, i1Term)
val Int.i4Term get() = i4(ushr(2).i2Term, i2Term)
val Int.i8Term get() = i8(ushr(4).i4Term, i4Term)
val Int.i16Term get() = i16(ushr(8).i8Term, i8Term)
val Int.i32Term get() = i32(ushr(16).i16Term, i16Term)

val Term.i1Int get() = if (this == zeroBit) 0 else 1
val Term.i2Int get() = pairAt0.i1Int.shl(1).or(pairAt1.i1Int)
val Term.i4Int get() = pairAt0.i2Int.shl(2).or(pairAt1.i2Int)
val Term.i8Int get() = pairAt0.i4Int.shl(4).or(pairAt1.i4Int)
val Term.i16Int get() = pairAt0.i8Int.shl(8).or(pairAt1.i8Int)
val Term.i32Int get() = pairAt0.i16Int.shl(16).or(pairAt1.i16Int)

val Term.i2Hi get() = pairAt0
val Term.i2Lo get() = pairAt1
val Term.i4Hi get() = pairAt0
val Term.i4Lo get() = pairAt1
val Term.i8Hi get() = pairAt0
val Term.i8Lo get() = pairAt1
val Term.i16Hi get() = pairAt0
val Term.i16Lo get() = pairAt1
val Term.i32Hi get() = pairAt0
val Term.i32Lo get() = pairAt1

val Term.i1Eq get() = bitEq
val Term.i2Eq get() = term { i2 -> i2Hi.i1Eq(i2.i2Hi).bitAnd(i2Lo.i1Eq(i2.i2Lo)) }
val Term.i4Eq get() = term { i4 -> i4Hi.i2Eq(i4.i4Hi).bitAnd(i4Lo.i2Eq(i4.i4Lo)) }
val Term.i8Eq get() = term { i8 -> i8Hi.i4Eq(i8.i8Hi).bitAnd(i8Lo.i4Eq(i8.i8Lo)) }
val Term.i16Eq get() = term { i16 -> i16Hi.i8Eq(i16.i16Hi).bitAnd(i16Lo.i8Eq(i16.i16Lo)) }
val Term.i32Eq get() = term { i32 -> i32Hi.i16Eq(i32.i32Hi).bitAnd(i32Lo.i16Eq(i32.i32Lo)) }
