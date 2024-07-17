// 通过指令的方式来实现元素加载过渡效果
const DISTANCE = 100;
const DURATION = 500;

const map = new WeakMap()
const ob = new IntersectionObserver((entries) => {
    for (const entry of entries){
        // 该元素和视口相交
        if(entry.isIntersecting){
            // 播放该元素动画
            const animation = map.get(entry.target);
            if (animation){
                animation.play()
                ob.unobserve(entry.target)
            }
        }
    }
})

// 是否在视口之下
function isBelowViewport(el: HTMLElement){
    const rect = el.getBoundingClientRect()
    return rect.top - DISTANCE > window.innerHeight
}

export default {
    mounted(el: HTMLElement){
        if(!isBelowViewport){   //只有当元素在视口top值下面的时候才会触发animate
            return
        }
        const animation = el.animate([
            {
                transform: `translateY(${DISTANCE}px)`,
                opacity: 0
            },
            {
                transform: `translateY(0)`,
                opacity: 1
            }
        ],{
            duration: DURATION,
            easing: 'ease-in-out',
            fill: 'forwards',
        })
        animation.pause()
        ob.observe(el)
        map.set(el, animation)
    },
    unmounted(el: HTMLElement){
        ob.unobserve(el)
    }
}
