package com.example.testquest.presentation.main_activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.testquest.R
import com.example.testquest.databinding.ActivityMainBinding
import com.example.testquest.di.ComponentStorage
import com.example.testquest.di.feature_components.DaggerMainActivityComponent
import com.example.testquest.di.provideRootComponent
import com.example.testquest.presentation.dialogs.NationalityDialog
import com.example.testquest.presentation.fragments.DogFragment
import com.example.testquest.presentation.settings_activity.SettingsActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val DOG_URL = "dogUrl"

private const val DOGS_API = "https://dog.ceo/api/"
private const val NATIONALITIES_API = "https://api.nationalize.io/"

private const val BUNDLE_KEY = "names"

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding : ActivityMainBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory

    private var daggerComponentKey = "MainActivity"

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComponentStorage.provideComponent(daggerComponentKey) {
            DaggerMainActivityComponent.factory().create(
                appComponent = ComponentStorage.provideRootComponent()
            )
        }.inject(this)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycleScope.launchWhenStarted {

            viewModel.customApiErrorFlow
                .onEach { errorString ->
                    binding.errorsTextView.text = errorString
                }
                .launchIn(this)

            viewModel.dogApiErrorFlow
                .onEach { errorString ->
                    binding.errorsTextView.text = errorString
                }
                .launchIn(this)

            viewModel.dogImage
                .onEach(binding.dogsImageView::load)
                .launchIn(this)

            viewModel.customApiText
                .onEach { response ->
                    binding.customApiResponseTextView.text = response
                }
                .launchIn(this)

            viewModel.sharedPreferencesFlow
                .onEach {
                    when (it) {
                        DOGS_API -> {
                            binding.dogsImageView.visibility = View.VISIBLE
                            binding.dogsImageView.isEnabled = true

                            binding.nationalityEditText.visibility = View.INVISIBLE
                            binding.nationalityEditText.isEnabled = false

                            binding.buttonNationalize.visibility = View.INVISIBLE
                            binding.buttonNationalize.isEnabled = false

                            binding.customApiEditText.visibility = View.INVISIBLE
                            binding.customApiEditText.isEnabled = false

                            binding.customApiButton.visibility = View.INVISIBLE
                            binding.customApiButton.isEnabled = false

                            binding.customApiResponseTextView.visibility = View.INVISIBLE
                            binding.customApiResponseTextView.isEnabled = false

                            viewModel.loadDog()
                        }
                        NATIONALITIES_API -> {
                            binding.dogsImageView.visibility = View.INVISIBLE
                            binding.dogsImageView.isEnabled = false

                            binding.nationalityEditText.visibility = View.VISIBLE
                            binding.nationalityEditText.isEnabled = true

                            binding.buttonNationalize.visibility = View.VISIBLE
                            binding.buttonNationalize.isEnabled = true

                            binding.customApiEditText.visibility = View.INVISIBLE
                            binding.customApiEditText.isEnabled = false

                            binding.customApiButton.visibility = View.INVISIBLE
                            binding.customApiButton.isEnabled = false

                            binding.customApiResponseTextView.visibility = View.INVISIBLE
                            binding.customApiResponseTextView.isEnabled = false
                        }
                        "" -> {
                            binding.dogsImageView.visibility = View.INVISIBLE
                            binding.dogsImageView.isEnabled = false

                            binding.nationalityEditText.visibility = View.INVISIBLE
                            binding.nationalityEditText.isEnabled = false

                            binding.buttonNationalize.visibility = View.INVISIBLE
                            binding.buttonNationalize.isEnabled = false

                            binding.customApiEditText.visibility = View.VISIBLE
                            binding.customApiEditText.isEnabled = true

                            binding.customApiButton.visibility = View.VISIBLE
                            binding.customApiButton.isEnabled = true

                            binding.customApiResponseTextView.visibility = View.VISIBLE
                            binding.customApiResponseTextView.isEnabled = true
                        }
                    }
                }
                .launchIn(this)
        }

        binding.settingsImageButton.setOnClickListener {
            val toSettingsActivityIntent = Intent(this, SettingsActivity::class.java)
            startActivity(toSettingsActivityIntent)
        }

        binding.dogsImageView.setOnClickListener {
            val dogFragment = DogFragment()
            val dogBundle = Bundle()

            //тут можно использовать bundle, так как строчка маленькая
            dogBundle.putString(DOG_URL, viewModel.dogImage.value)
            dogFragment.arguments = dogBundle

            supportFragmentManager.commit {
                add(R.id.dogFrameLayout, dogFragment)
                show(dogFragment)
            }
        }

        binding.buttonNationalize.setOnClickListener {
            val dialog = NationalityDialog()
            val nationalityBundle = Bundle()
            val names = binding.nationalityEditText.text.toString()

            //тут можно использовать bundle, так как строчка маленькая
            nationalityBundle.putString(BUNDLE_KEY, names)
            dialog.arguments = nationalityBundle
            dialog.show(supportFragmentManager, "")
        }

        binding.customApiButton.setOnClickListener {
            if (!Patterns.WEB_URL.matcher(binding.customApiEditText.text.toString()).matches()) {
                binding.errorsTextView.text = "Wrong url"
                binding.customApiResponseTextView.text = ""
            } else {
                binding.errorsTextView.text = ""

                val url = binding.customApiEditText.text.toString()

                viewModel.setBaseApiToCustom(url)
                viewModel.loadCustomApiContent(url)
            }
        }
    }

    override fun onDestroy() {
        if(!isChangingConfigurations) {
            ComponentStorage.clearComponent(daggerComponentKey)
        }
        super.onDestroy()
    }
}