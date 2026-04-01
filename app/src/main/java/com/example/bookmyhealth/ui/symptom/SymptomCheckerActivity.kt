package com.example.bookmyhealth.ui.symptom

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.SymptomAdapter
import com.example.bookmyhealth.adapter.AnalysisResultAdapter
import com.example.bookmyhealth.data.model.SymptomCategory
import com.example.bookmyhealth.databinding.ActivitySymptomCheckerBinding
import com.example.bookmyhealth.viewmodel.SymptomViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.Calendar

class SymptomCheckerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySymptomCheckerBinding
    private val viewModel: SymptomViewModel by viewModels()
    private lateinit var symptomAdapter: SymptomAdapter
    private lateinit var resultAdapter: AnalysisResultAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomCheckerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.init(this)

        setupUI()
        setupCategoryChips()
        setupRecyclerView()
        setupSearch()
        setupButtons()
        setupBottomSheet()
        observeViewModel()
    }

    private fun setupUI() {
        // Set greeting based on time
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.tvGreeting.text = when {
            hour < 12 -> "Good Morning!"
            hour < 17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { _, checkedIds ->
            val category = when (checkedIds.firstOrNull()) {
                R.id.chipGeneral -> SymptomCategory.GENERAL
                R.id.chipPain -> SymptomCategory.PAIN
                R.id.chipDigestive -> SymptomCategory.DIGESTIVE
                R.id.chipRespiratory -> SymptomCategory.RESPIRATORY
                R.id.chipSkin -> SymptomCategory.SKIN
                R.id.chipNeurological -> SymptomCategory.NEUROLOGICAL
                else -> null
            }
            viewModel.selectCategory(category)
        }
    }

    private fun setupRecyclerView() {
        symptomAdapter = SymptomAdapter(
            onSymptomClick = { symptom ->
                viewModel.toggleSymptomSelection(symptom)
            }
        )

        binding.rvSymptoms.apply {
            layoutManager = GridLayoutManager(this@SymptomCheckerActivity, 2)
            adapter = symptomAdapter
        }

        resultAdapter = AnalysisResultAdapter()
        binding.rvResults.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SymptomCheckerActivity)
            adapter = resultAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.searchSymptoms(text?.toString() ?: "")
        }
    }

    private fun setupButtons() {
        binding.fabAnalyze.setOnClickListener {
            viewModel.analyzeSymptoms()
        }

        binding.btnClearAll.setOnClickListener {
            viewModel.clearAllSelections()
        }

        binding.btnVoice.setOnClickListener {
            // Voice recognition - implement with SpeechRecognizer
        }

        binding.btnFilter.setOnClickListener {
            // Show filter dialog
        }

        binding.btnSaveResults.setOnClickListener {
            // Save result to history
            hideBottomSheet()
        }

        binding.btnCloseResults.setOnClickListener {
            hideBottomSheet()
        }

        binding.btnShare.setOnClickListener {
            shareResults()
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetResults)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun observeViewModel() {
        viewModel.filteredSymptoms.observe(this) { symptoms ->
            symptomAdapter.submitList(symptoms.toList())
        }

        viewModel.selectedSymptoms.observe(this) { selected ->
            val count = selected.size
            binding.tvSelectedCount.text = "$count selected"
            binding.progressBar.progress = (count * 100) / 12
            binding.fabAnalyze.isEnabled = count >= 3

            if (count == 0) {
                binding.btnClearAll.visibility = View.GONE
            } else {
                binding.btnClearAll.visibility = View.VISIBLE
            }
        }

        viewModel.analysisResults.observe(this) { results ->
            if (results.isNotEmpty()) {
                resultAdapter.submitList(results)
                binding.tvResultDate.text = "Based on ${viewModel.getSelectedCount()} symptoms"

                // Show warning for high severity
                val hasHighSeverity = results.any { it.confidence > 70 }
                binding.cardWarning.visibility = if (hasHighSeverity) View.VISIBLE else View.GONE

                showBottomSheet()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.analysisCount.observe(this) { count ->
            binding.tvAnalysisCount.text = count.toString()
        }
    }

    private fun showBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun shareResults() {
        val results = viewModel.analysisResults.value ?: return
        val text = buildString {
            appendLine("🩺 AI Symptom Analysis Results")
            appendLine("━━━━━━━━━━━━━━━━━━━━")
            results.forEachIndexed { index, result ->
                appendLine("${index + 1}. ${result.diseaseName}")
                appendLine("   Confidence: ${result.confidence}%")
                appendLine("   Consult: ${result.doctorType}")
                appendLine()
            }
            appendLine("⚠️ This is not a medical diagnosis. Please consult a doctor.")
        }

        val intent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(android.content.Intent.createChooser(intent, "Share Results"))
    }
}