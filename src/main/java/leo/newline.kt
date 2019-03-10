package leo

import leo.base.EnumBit
import leo.base.Stream
import leo.base.bitStream
import leo.base.clampedByte

val newlineChar = '\n'
val newlineByte = newlineChar.clampedByte
val newlineBitStream: Stream<EnumBit> = newlineByte.bitStream