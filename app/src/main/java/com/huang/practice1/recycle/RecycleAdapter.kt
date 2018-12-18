package com.huang.practice1.recycle

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.huang.practice1.R
import com.huang.practice1.process.ProcessView
import com.huang.practice1.process.ProcessView1
import kotlinx.android.synthetic.main.item_recycle.view.*
import java.util.*


/**
 * Des:
 * Created by huang on 2018/9/24 0024 14:45
 */
class RecycleAdapter(var mContext: Context) : BaseRecyclerAdapter<RecycleAdapter.ViewHolder, Int>(mContext) {


    override fun mOnCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_recycle, parent, false))
    }

    override fun mOnBindViewHolder(holder: ViewHolder, position: Int, data: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }


    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view), ProcessView.OnItemClickListener, ProcessView1.OnItemClickListener {
        override fun onItemClick(vPosition: Int, hPosition: Int, text: String) {
            Log.e("---", "vPosition: $vPosition--hPosition: $hPosition")
            Toast.makeText(this@RecycleAdapter.mContext
                    , "vPosition: $vPosition  --hPosition: $hPosition"
                    , Toast.LENGTH_SHORT).show()
        }

        init {
            view.process_view1.setOnItemClickListener(this)
            //view.process_view1.setTexts(getTextShow())
        }
    }

    private fun getTextShow(): HashMap<Int, List<String>> {
        val text1 = Arrays.asList("执行通知")
        val text2 = Arrays.asList("送达文书")
        val text3 = Arrays.asList("强行措施", "财产调查", "解除措施")
        val text4 = Arrays.asList("执行通知", "通知", "执行通知", "执行通知")
        val text5 = Arrays.asList("执行通知")
        val text6 = Arrays.asList("行通", "执通", "通知", "执行", "执行", "执知")
        val text7 = Arrays.asList("执行通", "执通知", "执行知")
        val map = HashMap<Int, List<String>>()
        map[0] = text1
        map[1] = text2
        map[2] = text3
        map[3] = text4
        map[4] = text5
        map[5] = text6
        map[6] = text7
        return map
    }
}