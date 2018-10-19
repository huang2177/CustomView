package com.huang.practice1.recycle

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 实现点击事件、HeaderView、FooterView的RecyclerView 适配器
 *
 * @param <VH> 内容Item的ViewHolder类型
 * @param <T>  数据的类型
 */
abstract class BaseRecyclerAdapter<VH : ViewHolder, T>(mContext: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var onItemClick: OnItemClick? = null //Item点击事件接口
    private var onLongItemClick: OnLongItemClick? = null //item长按事件接口
    protected var data: ArrayList<T>? = ArrayList()  //数据源
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val mHeaderViews = SparseArrayCompat<View>() //header集合
    private val mFooterViews = SparseArrayCompat<View>() //foot 集合

    val isEmpty: Boolean
        get() = data!!.isEmpty()

    /**
     * 头部总数
     *
     * @return
     */
    val headersCount: Int
        get() = mHeaderViews.size()

    /**
     * 尾部总数
     *
     * @return
     */
    val footersCount: Int
        get() = mFooterViews.size()

    /**
     * 内容长度
     *
     * @return
     */
    protected val realItemCount: Int
        get() = data!!.size

    abstract fun mOnCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun mOnBindViewHolder(holder: VH, position: Int, data: T)


    /**
     * 添加数据
     *
     * @param data
     */
    fun addData(data: List<T>, notify: Boolean = true) {
        this.data!!.clear()
        this.data!!.addAll(data)
        if (notify) {
            notifyDataSetChanged()
        }
    }

    fun notifyDataChanged(isRefresh: Boolean, list: List<T>?) {
        if (isRefresh) {
            this.data!!.clear()
            if (list != null && list.size > 0) {
                this.data!!.addAll(list)
            }
            notifyDataSetChanged()
        } else {
            if (list != null && list.size > 0) {
                this.data!!.addAll(list)
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 清除数据
     */
    fun clearData() {
        if (data != null && data!!.size != 0) {
            data!!.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * 获取当前数据集
     *
     * @return
     */
    fun getData(): List<T>? {
        return data
    }

    /**
     * 添加头部
     * PS:若头部偏左显示，加上headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
     *
     * @param headerView
     * @return
     */
    fun addHeaderView(headerView: View) {
        headerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mHeaderViews.put(TYPE_HEADER + mHeaderViews.size(), headerView)
        notifyItemChanged(mHeaderViews.size() - 1)
    }

    /**
     * 添加尾部
     *
     * @param footView
     * @return
     */
    fun addFooterView(footView: View) {
        if (footView.layoutParams == null) {
            footView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        mFooterViews.put(TYPE_FOOT + mFooterViews.size(), footView)
        notifyItemChanged(itemCount - 1)
    }

    /**
     * 添加尾部
     *
     * @param footView
     * @return
     */
    fun addFooterView(footView: View, height: Int) {
        footView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        mFooterViews.put(TYPE_FOOT + mFooterViews.size(), footView)
        notifyItemChanged(itemCount - 1)
    }

    /**
     * 删除所有尾部
     *
     * @return
     */
    fun clearFooterView() {
        if (mFooterViews.size() != 0) {
            mFooterViews.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * 当前item是否是头布局
     *
     * @param position
     * @return
     */
    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    /**
     * 当前item是否是尾布局
     *
     * @param position
     * @return
     */
    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + realItemCount
    }

    fun setOnItemClickListener(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setonLongItemClickListener(onLongItemClick: OnLongItemClick) {
        this.onLongItemClick = onLongItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mHeaderViews.get(viewType) != null) {
            return HeaderViewHolder(mHeaderViews.get(viewType))
        } else if (mFooterViews.get(viewType) != null) {
            return FooterViewHolder(mFooterViews.get(viewType))
        }
        return mOnCreateViewHolder(parent, viewType)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var position = position
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return
        }
        if (onItemClick != null) {
            holder.itemView.setOnClickListener { v ->
                val mPosition = holder.layoutPosition - mHeaderViews.size()
                onItemClick!!.onItemClick(v, mPosition)
            }
        }
        if (onLongItemClick != null) {
            holder.itemView.setOnLongClickListener { v ->
                val mPosition = holder.layoutPosition - mHeaderViews.size()
                onLongItemClick!!.onLongItemClick(v, holder, mPosition)
                true
            }
        }
        position -= mHeaderViews.size()
        mOnBindViewHolder(holder as VH, position, data!![position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeaderViewPos(position)) {
            mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            mFooterViews.keyAt(position - headersCount - realItemCount)
        } else {
            position - headersCount
        }
    }

    override fun getItemCount(): Int {
        return headersCount + realItemCount + footersCount
    }


    internal class HeaderViewHolder(itemView: View) : ViewHolder(itemView)

    internal class FooterViewHolder(itemView: View) : ViewHolder(itemView)

    //设置HeaderView与FooterView兼容GridLayoutManager
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = getItemViewType(position)
                    return if (mHeaderViews.get(viewType) != null || mFooterViews.get(viewType) != null) {
                        layoutManager.spanCount
                    } else 1
                }
            }
        }
    }

    //设置HeaderView与FooterView兼容StaggeredGridLayoutManager
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    /**
     * recycleView Item点击事件
     * xiejingwen
     */
    interface OnItemClick {
        fun onItemClick(view: View, position: Int)
    }

    /**
     * recycleView Item长按事件
     * xiejingwen
     */
    interface OnLongItemClick {
        fun onLongItemClick(view: View, holder: ViewHolder, position: Int)
    }

    companion object {
        protected val TYPE_HEADER = 10000 //head类型
        protected val TYPE_FOOT = 20000   //foot类型
        protected val TYPE_CONTENT = 0   //内容类型
    }
}
/**
 * 添加数据
 *
 * @param data
 */

