package com.seanshubin.detangler.model

case class Reason(from: Single, to: Single, reasons: Set[Reason])
