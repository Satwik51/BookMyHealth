package com.example.bookmyhealth.data.model

import com.google.gson.annotations.SerializedName

// ── API envelope ──────────────────────────────────────────────────────────────
data class DrugResponse(
    val results: List<Drug>?,
    val meta: DrugMeta?           // total hit count, pagination info
)

data class DrugMeta(
    val results: DrugMetaResults?
)

data class DrugMetaResults(
    val skip: Int?,
    val limit: Int?,
    val total: Int?               // useful for "showing X of Y results"
)

// ── Core drug label ───────────────────────────────────────────────────────────
data class Drug(

    // Identity
    val id: String?,              // FDA set_id — stable unique identifier
    val openfda: OpenFda?,

    // What it's for
    val purpose: List<String>?,
    val indications_and_usage: List<String>?,

    // How to use it
    val dosage_and_administration: List<String>?,
    val dosage_forms_and_strengths: List<String>?,

    // Safety
    val warnings: List<String>?,
    val warnings_and_cautions: List<String>?,
    val contraindications: List<String>?,
    val adverse_reactions: List<String>?,
    val drug_interactions: List<String>?,

    // Storage & handling
    val storage_and_handling: List<String>?,

    // Special populations
    val pregnancy: List<String>?,
    val pediatric_use: List<String>?,
    val geriatric_use: List<String>?,

    // OTC active ingredient (good for stat chips)
    val active_ingredient: List<String>?,

    // Keep-out / stop-use (OTC labels)
    @SerializedName("keep_out_of_reach_of_children")
    val keepOutOfReachOfChildren: List<String>?,

    val stop_use: List<String>?,
    val ask_doctor: List<String>?
) {
    // ── Convenience helpers (used in Activity/ViewModel) ──────────────────────

    /** Best display name: brand first, generic fallback. */
    val displayName: String
        get() = openfda?.brand_name?.firstOrNull()
            ?: openfda?.generic_name?.firstOrNull()
            ?: "Unknown"

    /** Generic name for the subtitle line in the hero card. */
    val genericName: String?
        get() = openfda?.generic_name?.firstOrNull()

    /** Manufacturer for the hero subtitle. */
    val manufacturer: String?
        get() = openfda?.manufacturer_name?.firstOrNull()

    /**
     * Best usage text: prefers purpose (short, OTC-style),
     * falls back to indications_and_usage (longer, Rx-style).
     */
    val bestUsage: List<String>?
        get() = purpose?.takeIf { it.isNotEmpty() }
            ?: indications_and_usage

    /**
     * Best warnings text: merges warnings + warnings_and_cautions
     * so neither Rx nor OTC labels come back empty.
     */
    val bestWarnings: List<String>?
        get() {
            val combined = (warnings.orEmpty() + warnings_and_cautions.orEmpty())
                .filter { it.isNotBlank() }
            return combined.takeIf { it.isNotEmpty() }
        }
}

// ── OpenFDA metadata block ────────────────────────────────────────────────────
data class OpenFda(
    val brand_name: List<String>?,
    val generic_name: List<String>?,
    val manufacturer_name: List<String>?,
    val product_type: List<String>?,      // "HUMAN OTC DRUG" / "HUMAN PRESCRIPTION DRUG"
    val route: List<String>?,             // "ORAL", "TOPICAL", etc.
    val substance_name: List<String>?,    // active substance(s)

    @SerializedName("rxcui")
    val rxCui: List<String>?,             // RxNorm concept ID — useful for cross-referencing

    @SerializedName("unii")
    val unii: List<String>?               // FDA UNII identifier
) {
    /** Human-readable product type badge: "OTC" or "Rx". */
    val productTypeBadge: String
        get() = when {
            product_type?.any { it.contains("OTC", ignoreCase = true) } == true -> "OTC"
            product_type?.any { it.contains("PRESCRIPTION", ignoreCase = true) } == true -> "Rx"
            else -> "—"
        }

    /** Route formatted for display, e.g. "Oral · Tablet". */
    val routeDisplay: String?
        get() = route?.joinToString(" · ") { it.lowercase().replaceFirstChar { c -> c.uppercase() } }
            ?.takeIf { it.isNotBlank() }
}