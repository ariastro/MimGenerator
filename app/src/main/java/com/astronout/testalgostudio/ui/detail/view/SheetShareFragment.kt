package com.astronout.testalgostudio.ui.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.astronout.testalgostudio.R
import kotlinx.android.synthetic.main.fragment_sheet_share.*

class SheetShareFragment : SuperBottomSheetFragment() {

    private var path: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_sheet_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        share_to_fb.setOnClickListener {
            this.dismiss()
        }

        share_to_twitter.setOnClickListener {
            this.dismiss()
            (activity as DetailActivity).shareTwitter(path!!)
        }

    }

    override fun getCornerRadius() = requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getPeekHeight(): Int = 300

    fun setPath(path: String) {
        this.path = path
    }

}