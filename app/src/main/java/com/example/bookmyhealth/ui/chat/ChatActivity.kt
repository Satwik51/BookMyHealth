package com.example.bookmyhealth.ui.chat

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.adapter.ChatAdapter
import com.example.bookmyhealth.data.model.ChatMessage
import com.example.bookmyhealth.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageView

    private lateinit var introLayout: LinearLayout
    private lateinit var loadingLayout: LinearLayout
    private lateinit var tvTyping: TextView
    private lateinit var ivRobot: ImageView

    // 🔥 ViewModel
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⚠️ IMPORTANT (correct layout)
        setContentView(R.layout.activity_chat)

        initViews()
        setupRecycler()
        setupListeners()
        observeViewModel()
        startRobotAnimation()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        introLayout = findViewById(R.id.introLayout)
        loadingLayout = findViewById(R.id.loadingLayout)
        tvTyping = findViewById(R.id.tvTyping)
        ivRobot = findViewById(R.id.ivRobot)
    }

    private fun setupRecycler() {
        adapter = ChatAdapter(messageList)

        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        recyclerView.adapter = adapter
    }

    private fun setupListeners() {

        btnSend.setOnClickListener {

            val text = etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            // 🔥 Intro → Chat transition
            if (introLayout.visibility == View.VISIBLE) {
                animateIntroToChat()
            }

            viewModel.sendMessage(text) // ✅ REAL AI CALL
            etMessage.text.clear()
        }
    }

    // 🔥 OBSERVE VIEWMODEL
    private fun observeViewModel() {

        viewModel.messages.observe(this) { list ->
            messageList.clear()
            messageList.addAll(list)
            adapter.notifyDataSetChanged()
            scrollToBottom()
        }

        viewModel.typing.observe(this) { isTyping ->
            tvTyping.visibility = if (isTyping) View.VISIBLE else View.GONE
        }
    }

    // 🔄 SCROLL
    private fun scrollToBottom() {

        recyclerView.post {
            val size = messageList.size
            if (size > 0) {
                recyclerView.scrollToPosition(size - 1)
            }
        }
    }

    // 🔥 INTRO → CHAT ANIMATION
    private fun animateIntroToChat() {

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 300

        introLayout.startAnimation(fadeOut)
        introLayout.visibility = View.GONE

        recyclerView.visibility = View.VISIBLE

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 300

        recyclerView.startAnimation(fadeIn)
    }

    // 🔄 LOADING (optional use)
    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    // 🤖 ROBOT ANIMATION (PULSE)
    private fun startRobotAnimation() {

        val scale = ScaleAnimation(
            1f, 1.1f,
            1f, 1.1f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        )

        scale.duration = 800
        scale.repeatMode = ScaleAnimation.REVERSE
        scale.repeatCount = ScaleAnimation.INFINITE

        ivRobot.startAnimation(scale)
    }
}