package safeme.uz.data.local.sharedpreference

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import safeme.uz.utils.SharedPreference
import javax.inject.Singleton

@Singleton
class AppSharedPreference(@ApplicationContext context: Context) : SharedPreference(context) {
    var token: String by StringPreference("")
    var refresh: String by StringPreference("")
    var pinCode: String by StringPreference("")
    var locale: String by StringPreference("uz")
    var sessionId: String by StringPreference("")
    var bookmarkGame:Boolean by BooleanPreference()
    var languageSaved:Boolean by BooleanPreference()
}