package net.suntrans.tenement

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import net.suntrans.tenement.persistence.AppDatabase
import net.suntrans.tenement.persistence.User

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("net.suntrans.tenement", appContext.packageName)
    }

    @Test
    fun appDatabase(){
        val appContext = InstrumentationRegistry.getTargetContext()

        val instance = AppDatabase.getInstance(appContext)

        val use =User()
        use.id=59
       instance.userDao().delete(use)



    }
}
