package com.seanshubin.detangler.bytecode

import java.io.DataInput

case class ClassFileInfo(magic: Int,
                         minorVersion: Short,
                         majorVersion: Short,
                         constantPoolCountPlusOne: Short,
                         constantPool: Seq[ConstantPoolInfo],
                         accessFlags: Short,
                         thisClass: Short) {
  def thisClassName: String = {
    val ClassInfo(nameIndex) = constantPool(thisClass)
    val Utf8Info(name) = constantPool(nameIndex)
    name
  }

  def dependencyNames: Seq[String] = {
    val classNameIndices = constantPool.collect {
      case ClassInfo(nameIndex) => nameIndex
    }
    val classNames = classNameIndices.map(classNameIndexToName)

    def nameMatchesThis(name: String) = name == thisClassName

    val classNamesBesidesThis = classNames.filterNot(nameMatchesThis)
    classNamesBesidesThis
  }

  def classNameIndexToName(classNameIndex: Short): String = constantPool(classNameIndex) match {
    case Utf8Info(className) => className
    case _ => throw new RuntimeException(s"expected string at index $classNameIndex")
  }

  def toMultipleLineString: Seq[String] = {
    Seq(
      s"magic = $magic",
      s"minorVersion = $minorVersion",
      s"majorVersion = $majorVersion",
      s"constantPoolCountPlusOne = $constantPoolCountPlusOne") ++
      constantPool.map(constantPoolInfoString) ++
      Seq(
        s"accessFlags = $accessFlags",
        s"thisClass = $thisClass")
  }

  def constantPoolInfoString(constantPoolInfo: ConstantPoolInfo): String = {
    constantPoolInfo match {
      case ClassInfo(nameIndex) =>
        val name = classNameIndexToName(nameIndex)
        constantPoolInfo.toString + " " + name
      case _ => constantPoolInfo.toString
    }
  }
}

sealed trait ConstantPoolInfo

case class ClassInfo(nameIndex: Short) extends ConstantPoolInfo

case class FieldRefInfo(classIndex: Short, nameAndTypeIndex: Short) extends ConstantPoolInfo

case class MethodRefInfo(classIndex: Short, nameAndTypeIndex: Short) extends ConstantPoolInfo

case class InterfaceMethodRefInfo(classIndex: Short, nameAndTypeIndex: Short) extends ConstantPoolInfo

case class StringInfo(stringIndex: Short) extends ConstantPoolInfo

case class IntegerInfo(value: Int) extends ConstantPoolInfo

case class FloatInfo(value: Float) extends ConstantPoolInfo

case class LongInfo(value: Long) extends ConstantPoolInfo

case class DoubleInfo(value: Double) extends ConstantPoolInfo

case class NameAndTypeInfo(nameIndex: Short, descriptorIndex: Short) extends ConstantPoolInfo

case class Utf8Info(value: String) extends ConstantPoolInfo {
  override def toString: String = s"Utf8Info((${value.size})'${escape(value)}')"

  private def escape(target: String) = {
    target.flatMap {
      case '\n' => "\\n"
      case '\b' => "\\b"
      case '\t' => "\\t"
      case '\f' => "\\f"
      case '\r' => "\\r"
      case '\"' => "\\\""
      case '\'' => "\\\'"
      case '\\' => "\\\\"
      case x => x.toString
    }
  }
}

case class MethodHandleInfo(referenceKind: Byte, referenceIndex: Short) extends ConstantPoolInfo

case class MethodTypeInfo(descriptorIndex: Short) extends ConstantPoolInfo

case class InvokeDynamicInfo(bootstrapMethodAttrIndex: Short, nameAndTypeIndex: Short) extends ConstantPoolInfo

case object Unusable extends ConstantPoolInfo

object ClassFileInfo {
  val TagClass = 7
  val TagFieldref = 9
  val TagMethodref = 10
  val TagInterfaceMethodref = 11
  val TagString = 8
  val TagInteger = 3
  val TagFloat = 4
  val TagLong = 5
  val TagDouble = 6
  val TagNameAndType = 12
  val TagUtf8 = 1
  val TagMethodHandle = 15
  val TagMethodType = 16
  val TagInvokeDynamic = 18

  def fromDataInput(in: DataInput): ClassFileInfo = {
    val magic = in.readInt
    val minorVersion = in.readShort()
    val majorVersion = in.readShort()
    val constantPoolCount = in.readShort()

    def readRemainingConstants(soFar: Seq[ConstantPoolInfo], remainingIndices: Int): Seq[ConstantPoolInfo] = {
      if (remainingIndices == 0) soFar
      else {
        readConstant(in) match {
          case takesTwoSlots@(_: DoubleInfo | _: LongInfo) => readRemainingConstants(soFar :+ takesTwoSlots :+ Unusable, remainingIndices - 2)
          case takesOneSlot => readRemainingConstants(soFar :+ takesOneSlot, remainingIndices - 1)
        }
      }
    }

    val constantPool = readRemainingConstants(Seq(Unusable), constantPoolCount - 1)
    val accessFlags = in.readShort()
    val thisClass = in.readShort()
    val classFileInfo = ClassFileInfo(magic, minorVersion, majorVersion, constantPoolCount, constantPool, accessFlags, thisClass)
    classFileInfo
  }

  def readConstant(in: DataInput): ConstantPoolInfo = {
    val tag = in.readByte()
    val info: ConstantPoolInfo = tag match {
      case TagClass =>
        val nameIndex = in.readShort
        ClassInfo(nameIndex)
      case TagFieldref =>
        val classIndex = in.readShort()
        val nameAndTypeIndex = in.readShort()
        FieldRefInfo(classIndex, nameAndTypeIndex)
      case TagMethodref =>
        val classIndex = in.readShort()
        val nameAndTypeIndex = in.readShort()
        MethodRefInfo(classIndex: Short, nameAndTypeIndex: Short)
      case TagInterfaceMethodref =>
        val classIndex = in.readShort()
        val nameAndTypeIndex = in.readShort()
        InterfaceMethodRefInfo(classIndex: Short, nameAndTypeIndex: Short)
      case TagString =>
        val stringIndex = in.readShort()
        StringInfo(stringIndex: Short)
      case TagInteger =>
        val value = in.readInt()
        IntegerInfo(value: Int)
      case TagFloat =>
        val value = in.readFloat()
        FloatInfo(value: Float)
      case TagLong =>
        val value = in.readLong()
        LongInfo(value: Long)
      case TagDouble =>
        val value = in.readDouble()
        DoubleInfo(value: Double)
      case TagNameAndType =>
        val nameIndex = in.readShort()
        val descriptorIndex = in.readShort()
        NameAndTypeInfo(nameIndex: Short, descriptorIndex: Short)
      case TagUtf8 =>
        val value = in.readUTF()
        Utf8Info(value)
      case TagMethodHandle =>
        val referenceKind = in.readByte()
        val referenceIndex = in.readShort()
        MethodHandleInfo(referenceKind, referenceIndex)
      case TagMethodType =>
        val descriptorIndex = in.readShort()
        MethodTypeInfo(descriptorIndex)
      case TagInvokeDynamic =>
        val bootstrapMethodAttrIndex = in.readShort()
        val nameAndTypeIndex = in.readShort()
        InvokeDynamicInfo(bootstrapMethodAttrIndex, nameAndTypeIndex)
      case tagUnknown => throw new RuntimeException(s"Don't know how to handle constant pool tag $tagUnknown")
    }
    info
  }
}
