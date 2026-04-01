package com.example.bookmyhealth.ui.drug

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.SuggestionAdapter
import com.example.bookmyhealth.data.model.OpenFda
import com.example.bookmyhealth.viewmodel.DrugViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class DrugSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: DrugViewModel

    // Search
    private lateinit var etSearch: AutoCompleteTextView

    // Hero card fields
    private lateinit var tvDrugName: TextView
    private lateinit var tvDrugSubtitle: TextView

    // Quick stat chips
    private lateinit var tvStatDose: TextView
    private lateinit var tvStatFrequency: TextView
    private lateinit var tvStatCourse: TextView

    // Content card text
    private lateinit var tvUsage: TextView
    private lateinit var tvDirections: TextView
    private lateinit var tvWarnings: TextView

    // Tabs
    private lateinit var tabOverview: TextView
    private lateinit var tabUsage: TextView
    private lateinit var tabDirections: TextView

    // Cards (for show/hide)
    private lateinit var usageCard: MaterialCardView
    private lateinit var directionsCard: MaterialCardView
    private lateinit var warningsCard: MaterialCardView

    // Bottom CTA
    private lateinit var btnConsult: MaterialButton

    // Nullable — safe if ProgressBar is not yet added to the XML
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_search)

        initViews()
        initViewModel()
        setupTabs()
        setupSearch()
        observeData()
    }

    // ─── INIT VIEWS ──────────────────────────────────────────────────────────
    private fun initViews() {
        etSearch        = findViewById(R.id.Etsearch)

        tvDrugName      = findViewById(R.id.drugname)
        tvDrugSubtitle  = findViewById(R.id.drugSubtitle)

        tvStatDose      = findViewById(R.id.tvStatDose)
        tvStatFrequency = findViewById(R.id.tvStatFrequency)
        tvStatCourse    = findViewById(R.id.tvStatCourse)

        tvUsage         = findViewById(R.id.tvUsage)
        tvDirections    = findViewById(R.id.tvDirections)
        tvWarnings      = findViewById(R.id.tvWarnings)

        tabOverview     = findViewById(R.id.tabOverview)
        tabUsage        = findViewById(R.id.tabUsage)
        tabDirections   = findViewById(R.id.tabDirections)

        usageCard       = findViewById(R.id.usageCard)
        directionsCard  = findViewById(R.id.directionsCard)
        warningsCard    = findViewById(R.id.warningsCard)

        btnConsult      = findViewById(R.id.bottomBar)

        // Safe — returns null without crashing if view not in layout yet
        progressBar     = findViewById(R.id.progressBar)

        etSearch.threshold = 1

        // Default state on launch
        setActiveTab(tabOverview)
        showOverview()
    }

    // ─── VIEWMODEL ───────────────────────────────────────────────────────────
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[DrugViewModel::class.java]
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────
    private fun setupSearch() {

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.text.toString())
                true
            } else false
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.length >= 2) viewModel.fetchSuggestions(query)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etSearch.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()
            etSearch.setText(selected)
            etSearch.setSelection(selected.length)
            hideKeyboard()
            performSearch(selected)
        }
    }

    // ─── TABS ────────────────────────────────────────────────────────────────
    private fun setupTabs() {
        tabOverview.setOnClickListener   { setActiveTab(tabOverview);   showOverview()       }
        tabUsage.setOnClickListener      { setActiveTab(tabUsage);      showUsageOnly()      }
        tabDirections.setOnClickListener { setActiveTab(tabDirections); showDirectionsOnly() }
    }

    private fun setActiveTab(active: TextView) {
        listOf(tabOverview, tabUsage, tabDirections).forEach { tab ->
            tab.isSelected = false
            tab.setTextColor(getColor(R.color.tabInactiveText))
        }
        active.isSelected = true
        active.setTextColor(getColor(R.color.tabActiveText))
    }

    private fun showOverview() {
        usageCard.visibility      = View.VISIBLE
        directionsCard.visibility = View.VISIBLE
        warningsCard.visibility   = View.VISIBLE
    }

    private fun showUsageOnly() {
        usageCard.visibility      = View.VISIBLE
        directionsCard.visibility = View.GONE
        warningsCard.visibility   = View.GONE
    }

    private fun showDirectionsOnly() {
        usageCard.visibility      = View.GONE
        directionsCard.visibility = View.VISIBLE
        warningsCard.visibility   = View.GONE
    }

    // ─── OBSERVE DATA ────────────────────────────────────────────────────────
    private fun observeData() {

        // Loading — ?. safe-call: no crash if progressBar not in XML yet
        viewModel.loading.observe(this) { isLoading ->
            progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
            etSearch.isEnabled = !isLoading
        }

        // Error Snackbar
        viewModel.error.observe(this) { message ->
            if (!message.isNullOrBlank()) {
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                    .setAction("OK") { viewModel.clearError() }
                    .show()
                viewModel.clearError()
            }
        }

        // Suggestions dropdown
        viewModel.suggestions.observe(this) { list ->
            if (!list.isNullOrEmpty()) {
                etSearch.setAdapter(SuggestionAdapter(this, list))
                etSearch.showDropDown()
            }
        }

        // Drug result
        viewModel.drugData.observe(this) { drug ->
            if (drug == null) return@observe

            // Hero
            tvDrugName.text     = drug.displayName
            tvDrugSubtitle.text = buildSubtitle(drug.openfda)

            // Quick stats
            parseDosageStats(drug.dosage_and_administration?.joinToString(" "))

            // Usage card — uses bestUsage helper (purpose → indications fallback)
            tvUsage.text = drug.bestUsage
                ?.joinToString("\n\n")
                ?.trimClean()
                ?: "No usage information available."

            // Directions card
            tvDirections.text = drug.dosage_and_administration
                ?.joinToString("\n\n")
                ?.trimClean()
                ?: "No directions available."

            // Warnings card — uses bestWarnings helper (merges both warning fields)
            tvWarnings.text = drug.bestWarnings
                ?.joinToString("\n\n")
                ?.trimClean()
                ?: "No warnings listed."

            // Reset to overview on every fresh result
            setActiveTab(tabOverview)
            showOverview()
        }
    }

    // ─── SEARCH TRIGGER ──────────────────────────────────────────────────────
    private fun performSearch(query: String) {
        val clean = query.trim()
        if (clean.isEmpty()) return

        tvDrugName.text     = clean
        tvDrugSubtitle.text = "Fetching details…"
        tvUsage.text        = "Loading…"
        tvDirections.text   = "Loading…"
        tvWarnings.text     = "Loading…"
        resetStats()

        viewModel.searchDrug(clean)
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    /**
     * Builds the hero subtitle from OpenFDA metadata.
     * e.g. "Oral · OTC"  or  "Film-coated tablets · Rx"
     */
    private fun buildSubtitle(openfda: OpenFda?): String {
        val parts = listOfNotNull(
            openfda?.routeDisplay,
            openfda?.productTypeBadge?.takeIf { it != "—" }
        )
        return parts.joinToString(" · ").ifBlank { "Film-coated tablets · Rx required" }
    }

    /**
     * Lightweight heuristic: extracts dose, frequency, and course duration
     * from the raw FDA dosage string to populate the three stat chips.
     */
    private fun parseDosageStats(raw: String?) {
        if (raw.isNullOrBlank()) { resetStats(); return }

        // Dose: "400 mg", "500mg"
        val dose = Regex("""(\d+)\s*mg""", RegexOption.IGNORE_CASE)
            .find(raw)?.groupValues?.get(1)?.let { "${it}mg" } ?: "—"

        // Frequency: "twice a day", "3 times daily", "every 6 hours"
        val freqRegex = Regex(
            """(once|twice|three times|(\d+)\s*times)\s*(a\s*day|daily|per\s*day)""",
            RegexOption.IGNORE_CASE
        )
        val freq = when {
            freqRegex.containsMatchIn(raw) -> {
                val m = freqRegex.find(raw)!!.groupValues[0].lowercase()
                when {
                    "once"        in m -> "1×"
                    "twice"       in m -> "2×"
                    "three times" in m -> "3×"
                    else -> Regex("""(\d+)\s*times""").find(m)
                        ?.groupValues?.get(1)?.let { "${it}×" } ?: "—"
                }
            }
            Regex("""every\s*4""").containsMatchIn(raw) -> "3–4×"
            Regex("""every\s*6""").containsMatchIn(raw) -> "4×"
            Regex("""every\s*8""").containsMatchIn(raw) -> "3×"
            else -> "—"
        }

        // Course: "7 days", "10-day"
        val course = Regex("""(\d+)[\s-]day""", RegexOption.IGNORE_CASE)
            .find(raw)?.groupValues?.get(1)?.let { "${it}d" } ?: "—"

        tvStatDose.text      = dose
        tvStatFrequency.text = freq
        tvStatCourse.text    = course
    }

    private fun resetStats() {
        tvStatDose.text      = "—"
        tvStatFrequency.text = "—"
        tvStatCourse.text    = "—"
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
        etSearch.clearFocus()
    }

    /** Collapses excessive whitespace and triple+ newlines from FDA label text. */
    private fun String.trimClean(): String =
        replace(Regex("""[ \t]+"""), " ")
            .replace(Regex("""(\n\s*){3,}"""), "\n\n")
            .trim()
}