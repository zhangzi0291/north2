import { createStore } from 'vuex'
import VueXAlong from 'vuex-along'

// 创建一个新的 index 实例
export default createStore({
    state : {
        menuInfo: {
            openKeys:[],
            selectedKeys:[]
        }
    },
    mutations: {
        setMenuInfo (state,data) {
            if(data.openKeys){
                state.menuInfo.openKeys = data.openKeys
            }
            if(data.selectedKeys) {
                state.menuInfo.selectedKeys = data.selectedKeys
            }
            console.log(state.menuInfo)
        }
    },
    plugins:[VueXAlong({
        name: 'along',     //存放在localStroage或者sessionStroage 中的名字
        // local: true,      //是否存放在local中  false 不存放 如果存放按照下面session的配置配
        session: { list: [], isFilter: true }
        //如果值不为false 那么可以传递对象 其中 当isFilter设置为true时， list 数组中的值就会被过滤调,这些值不会存放在seesion或者local中
    })]
})
