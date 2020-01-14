## Marquee

> 使用无限循环RecyclerView实现TextView的跑马灯效果

### MarqueeRecyclerView
1. 拦截并消费MotionEvent，禁止手动滑动和点击
```kotlin
override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
    return true
}

override fun onTouchEvent(e: MotionEvent?): Boolean {
    return true
}
```
2. 添加滑动监听，在滑动到最后一个时，重新滑动到首个
```kotlin
private fun setupView() {
    setOnScrollListener(object: OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // 只有滑动到target时才会IDLE
            if (newState == SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager as? MarqueeLayoutManager
                layoutManager?.let {
                    val position = it.findLastVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (position == itemCount - 1) {
                        layoutManager.scrollToPosition(0)
                        this@MarqueeRecyclerView.smoothScrollToPosition(itemCount)
                    }
                }
            }
        }
    })
}
```

### MarqueeAdapter
```kotlin
class MarqueeAdapter(private val mDataList: MutableList<String>, private val enableMarquee: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(wrapContent, matchParent)
            gravity = Gravity.CENTER_VERTICAL
            textSize = MARQUEE_TEXT_SIZE.toFloat()
            textColor = Color.WHITE
            singleLine = true
        }
        return Holder(textView)
    }
    // 2. 数据绑定时取余
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView as? TextView
        val message = mDataList.elementAtOrNull(position % mDataList.size)
        itemView?.let {
            it.text = message
        }
    }
    // 1. 设置itemCount为最大值
    override fun getItemCount(): Int {
        return if (enableMarquee && mDataList.isNotEmpty()) Int.MAX_VALUE else mDataList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
```

### MarqueeLayoutManager
> 自定义滚动速度，需要实现一个LinearSmoothScroller对象并重写smoothScrollToPosition()方法；
```
注意：需要实现一个空onTargetFound()方法，原因如下
RecyclerView.SmoothScroller implementation which uses a LinearInterpolator until the target position becomes a child of the RecyclerView and then uses a DecelerateInterpolator} to slowly approach to target position.
 ```

```kotlin
class MarqueeLayoutManager : LinearLayoutManager {

    private var linearSmoothScroller: MyLinearSmoothScroller? = null

    private var speedPerPixel: Float? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean, speedPerPixel: Float? = 15f) : super(context, orientation, reverseLayout) {
        this.speedPerPixel = speedPerPixel
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        if (linearSmoothScroller == null) {
            linearSmoothScroller = MyLinearSmoothScroller(recyclerView!!.context)
        }
        linearSmoothScroller?.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    inner class MyLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

        /**
         * 禁止使用DecelerateInterpolator
         */
        override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {

        }

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@MarqueeLayoutManager.computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return (speedPerPixel ?: 15f) / displayMetrics!!.density
        }
    }
}
```

### 两边的Fading效果
ListView、RecyclerView可以设置fadingEdgeLength和requiresFadingEdge属性实现

```xml
<com.capsane.example.homepage.marquee.MarqueeRecyclerView
    android:id="@+id/rv_marquee"
    android:layout_width="93dp"
    android:layout_height="match_parent"
    android:fadingEdgeLength="10dp"
    android:requiresFadingEdge="horizontal" />
```