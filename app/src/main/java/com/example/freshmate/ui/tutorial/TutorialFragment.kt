package com.example.freshmate.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmate.R
import com.example.freshmate.data.helper.TipsAdapter
import com.example.freshmate.data.response.Tips

class TutorialFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tipsAdapter: TipsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewTips)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tipsList = listOf(
            Tips(
                "Pastikan Pencahayaan Bagus",
                "Posisikan buah di tempat yang terang, seperti di dekat jendela atau di bawah cahaya lampu.",
                "Bayangan gelap atau pencahayaan redup.",
                R.drawable.cahaya
            ),
            Tips(
                "Pastikan Buah Terlihat Penuh dalam Frame",
                "Pastikan seluruh bagian buah masuk ke dalam layar kamera.",
                "Hanya sebagian buah yang terlihat atau gambar terpotong.",
                R.drawable.terpotong
            ),
            Tips(
                "Jangan Terlalu Dekat atau Terlalu Jauh",
                "Ambil jarak yang tepat sehingga buah terlihat jelas dan tidak blur.",
                "Kamera terlalu dekat (blur) atau terlalu jauh (tidak jelas).",
                R.drawable.jauh
            ),
            Tips(
                "Gunakan Latar Belakang Netral",
                "Pilih latar belakang polos atau sederhana agar fokus tetap pada buah.",
                "Latar yang ramai atau warna yang mirip dengan buah.",
                R.drawable.background
            ),
            Tips(
                "Pastikan Kamera Stabil",
                "Pegang ponsel dengan stabil atau gunakan tripod kecil jika perlu.",
                "Gambar yang goyang karena tangan tidak stabil.",
                R.drawable.blurr
            )
        )

        tipsAdapter = TipsAdapter(tipsList)
        recyclerView.adapter = tipsAdapter

        return view
    }
}