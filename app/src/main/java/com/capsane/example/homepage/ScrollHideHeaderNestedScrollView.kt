package com.capsane.example.homepage

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.dip
import kotlin.math.abs

class ScrollHideHeaderNestedScrollView : FrameLayout, NestedScrollingParent {

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    private var isScrolling: Boolean = false

    private var isAnimating: Boolean = false

    private val maxScrollY = dip(37)

    // FIXME: consumedYSum和this.scrollY之间的关系？
    //  scrollY表示相对与初始位置的偏移，consumedYSum用于统计一次滑动(+fling)中的偏移量:上/下，用于确定最终bar的显示(自动对齐)
    private var consumedYSum = 0

    private val scroller: Scroller by lazy(LazyThreadSafetyMode.NONE) { Scroller(context) }

    private var scrollListenerMap = hashMapOf<View, Boolean>()

    var onScrollChangedCallback: ((percent: Float) -> Unit)? = null

    var onHeaderViewScrollToHideCallback: ((hide: Boolean) -> Unit)? = null

    private val helper: NestedScrollingParentHelper by lazy { NestedScrollingParentHelper(this) }

    /**
     * child ACTION_DOWN时触发
     * 设为true，接受并处理child嵌套滑动
     */
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        isScrolling = true
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        helper.onNestedScrollAccepted(child, target, axes)
    }

    /**
     * 处理滑动时，bar的隐藏和显示
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // 上滑
        if (dy > 0 && this.scrollY == maxScrollY) {
            return
        }
        // 下滑
        if (dy < 0 && this.scrollY == 0) {
            return
        }
        // 下滑 && RV没有置顶
        val pos = (rv?.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
        if (dy < 0 && pos != 0) {
            return
        }
        // 上滑、下滑时，bar首先消耗dy；同时限制parent滑动范围在[0, maxScrollY]
        val destinationY = this.scrollY + dy
        val consumedY = when {
            destinationY in 0..maxScrollY -> dy
            destinationY > maxScrollY -> maxScrollY - this.scrollY
            else -> -this.scrollY
        }
        scrollBy(0, consumedY)
        consumed[1] = consumedY
        consumedYSum += consumedY
    }

    /**
     * 可以用于再次处理child没有用完的dy
     */
    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    /**
     * child ACTION_UP && there is a fling，判断是否需要处理嵌套fling
     * @return if true, fling will totally consumed by parent view
     * FIXME: NestedScrollingParent存在缺陷，https://www.jianshu.com/p/f55abc60a879
     *  可以尝试用NestedScrollingParent2，估计不用添加OnScrollListener了
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (scrollY == 0 && velocityY > 0) {    // FIXME: when?
            return true     // child do nothing
        }
        return false        // parent do nothing
    }

    /**
     * 正式处理嵌套fling：首次出现fling时，为rv添加OnScrollListener
     * 感觉也可以放到onNestedPreFling中处理
     */
    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        if (!scrollListenerMap.containsKey(rv)) {
            // remember to clear listeners
            rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // 下滑
                    if (dy < 0 && !isScrolling) {   // FIXME: !isScrolling?
                        val firstItemShow =
                            (rv.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() == 0
                        if (firstItemShow) {
                            animateToPosition(0)
                        }
                    }
                }
            })
            scrollListenerMap[target] = true
        }
        return false
    }

    /**
     * child ACTION_UP
     * 判断临界位置，进行自动补齐
     */
    override fun onStopNestedScroll(target: View) {
        isScrolling = false
        helper.onStopNestedScroll(target)
        if (consumedYSum > 0) {
            if (this.scrollY > maxScrollY / 4) {
                animateToPosition(maxScrollY)
            } else {
                animateToPosition(0)
            }
        } else {
            if (abs(this.scrollY - maxScrollY) > maxScrollY / 4) {
                animateToPosition(0)
            } else {
                animateToPosition(maxScrollY)
            }
        }
        consumedYSum = 0
    }


    /**
     * 标准写法？
     * scroller只负责处理数据，可以理解为插值策略；实际最终滑动还是scrollTo
     */
    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {   // True,表示Scroller的数值还在变化中
            val y = scroller.currY  // 当前变化的Y
            scrollTo(0, y)
            postInvalidate()
        }
//        super.computeScroll()
    }

    /**
     * 限制parent滑动范围[0, maxScrollY]
     * 其实没有必要，如果在onPreScroll中都做了判断的话
     */
    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
        onScrollChangedCallback?.invoke(y / maxScrollY.toFloat())
    }

    private fun animateToPosition(targetY: Int) {
        if (this.scrollY == targetY || isAnimating) {
            return
        }
        // TODO: ObjectAnimator
        val scrollAnimation = ObjectAnimator.ofInt(this, "scrollY", targetY)
        scrollAnimation.duration = 200
        scrollAnimation.start()
    }

}