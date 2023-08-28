package safeme.uz.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import java.util.Locale

class LocalHelper {

    companion object {

        fun changeLanguage(lang: String, context: Context) {
            val appSharedPreference = AppSharedPreference(context)
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val resource: Resources = context.resources
            val config: Configuration = resource.configuration
            config.setLocale(locale)
            resource.updateConfiguration(config, resource.displayMetrics)

        }

    }
}