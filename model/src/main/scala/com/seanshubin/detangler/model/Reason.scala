package com.seanshubin.detangler.model

case class Reason(from: Standalone, to: Standalone, reasons: Set[Reason])
