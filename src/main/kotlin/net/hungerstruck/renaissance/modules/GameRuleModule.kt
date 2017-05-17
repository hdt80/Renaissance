package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.spec.inject
import net.hungerstruck.renaissance.spec.module.RModule
import net.hungerstruck.renaissance.spec.module.RModuleContext

/**
 * Created by molenzwiebel on 21-12-15.
 */
class GameRuleModule(match: RMatch, modCtx: RModuleContext) : RModule(match, modCtx) {
    @inject var rules: List<Pair<String, Boolean>> = arrayListOf()

    override fun init() { }
}