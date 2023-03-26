package com.example.testquest.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.testquest.R
import com.example.testquest.databinding.NationalityDialogBinding
import com.example.testquest.di.ComponentStorage
import com.example.testquest.di.feature_components.DaggerNationalityDialogComponent
import com.example.testquest.di.provideRootComponent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val BUNDLE_KEY = "names"

class NationalityDialog: DialogFragment() {

    private var _binding: NationalityDialogBinding? = null
    private val binding: NationalityDialogBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: NationalityDialogViewModelFactory

    private var daggerComponentKey = "NationalityDialog"

    private val viewModel: NationalityDialogViewModel by viewModels { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = NationalityDialogBinding.inflate(layoutInflater)

        val positiveButtonClick = { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setPositiveButton("Закрыть", DialogInterface.OnClickListener(positiveButtonClick))
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nationality_dialog, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComponentStorage.provideComponent(daggerComponentKey) {
            DaggerNationalityDialogComponent.factory().create(
                appComponent = ComponentStorage.provideRootComponent()
            )
        }.inject(this)

        viewModel.loadNationalities(arguments?.getString(BUNDLE_KEY, "")!!.split(","))

        val response = StringBuilder()

        lifecycleScope.launchWhenStarted {
            viewModel.nationality
                .onEach {
                    it.map { nationality ->
                        nationality.country.forEach { country ->
                            response.append(
                                "CountryId = ${country.countryId}" + "\n" + "Probability = ${country.probability}" + "\n"
                            )
                        }
                        response.append("name = ${nationality.name}" + "\n")
                    }
                    binding.responseTextView.text = response.toString()
                }
                .launchIn(this)

            viewModel.errorFlow
                .onEach { errorString ->
                    binding.responseTextView.text = errorString
                }
                .launchIn(this)
        }
    }

    override fun onDetach() {
        if (!requireActivity().isChangingConfigurations) {
            ComponentStorage.clearComponent(daggerComponentKey)
        }
        super.onDetach()
    }
}