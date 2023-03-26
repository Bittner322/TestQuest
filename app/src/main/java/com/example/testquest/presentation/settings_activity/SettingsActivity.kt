package com.example.testquest.presentation.settings_activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.testquest.databinding.ActivitySettingsBinding
import com.example.testquest.di.ComponentStorage
import com.example.testquest.di.feature_components.DaggerSettingsActivityComponent
import com.example.testquest.di.provideRootComponent
import javax.inject.Inject

private const val SHARED_PREFS_NAME = "sharedPrefs"

private const val SHARED_PREFS_KEY = "sharedPrefsKey"

private const val DOGS_API_CHOSEN = "https://dog.ceo/api/"
private const val NATIONALITY_API_CHOSEN = "https://api.nationalize.io/"
private const val CUSTOM_API_CHOSEN = ""

class SettingsActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding: ActivitySettingsBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: SettingsActivityViewModelFactory

    private var daggerComponentKey = "SettingsActivity"

    private val viewModel: SettingsActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComponentStorage.provideComponent(daggerComponentKey) {
            DaggerSettingsActivityComponent.factory().create(
                appComponent = ComponentStorage.provideRootComponent()
            )
        }.inject(this)

        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)

        if (!sharedPrefs.contains(SHARED_PREFS_KEY)) {
            sharedPrefs.edit {
                putString(SHARED_PREFS_KEY, DOGS_API_CHOSEN)
            }
            binding.dogsApiRadioButton.isChecked = true
        } else {
            if (sharedPrefs.getString(SHARED_PREFS_KEY, DOGS_API_CHOSEN) == DOGS_API_CHOSEN) {
                binding.dogsApiRadioButton.isChecked = true
            } else if (sharedPrefs.getString(SHARED_PREFS_KEY, DOGS_API_CHOSEN) == NATIONALITY_API_CHOSEN) {
                binding.nationalityApiRadioButton.isChecked = true
            } else {
                binding.customApiRadioButton.isChecked = true
            }
        }

        binding.dogsApiRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setBaseApiToDog()

                sharedPrefs.edit {
                    putString(SHARED_PREFS_KEY, DOGS_API_CHOSEN)
                }
            }
        }

        binding.nationalityApiRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setBaseApiToNationalize()

                sharedPrefs.edit {
                    putString(SHARED_PREFS_KEY, NATIONALITY_API_CHOSEN)
                }
            }
        }

        binding.customApiRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPrefs.edit {
                    putString(SHARED_PREFS_KEY, CUSTOM_API_CHOSEN)
                }
            }
        }

        binding.backImageButton.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        if(!isChangingConfigurations) {
            ComponentStorage.clearComponent(daggerComponentKey)
        }
        super.onDestroy()
    }
}