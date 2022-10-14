package com.meteor.app.test

import org.springframework.util.StopWatch

class TestTimeUtil {

    companion object {
        fun time(process: Runnable): Long {
            val stopWatch = StopWatch()

            stopWatch.start()
            process.run()
            stopWatch.stop()
            return stopWatch.totalTimeMillis
        }
    }

}