package safeme.uz.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class LocalHelper {

    companion object {

        fun changeLanguage(lang: String, context: Context) {
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)

        }

    }
}