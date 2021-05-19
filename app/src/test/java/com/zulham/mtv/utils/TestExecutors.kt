package com.zulham.mtv.utils

import java.util.concurrent.Executor

object TestExecutors : Executor {
    override fun execute(command: Runnable) {
        command.run()
    }
}