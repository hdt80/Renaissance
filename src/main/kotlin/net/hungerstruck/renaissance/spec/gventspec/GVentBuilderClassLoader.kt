package net.hungerstruck.renaissance.spec.gventspec

import net.hungerstruck.renaissance.spec.SpecBuilderClassLoader
import java.io.File

class GVentBuilderClassLoader(path: File, override val className: String = "GVent") : SpecBuilderClassLoader(path)
