<template>
  <div class="node " :class="status">
<!--    <div v-if="loading" class="node-spin">-->
<!--      <img class="loading" :src="imgCot.loading" alt="" @click="openSetting"/>-->
<!--    </div>-->
    <div class="node-header">
      <div class="label">{{ label }}</div>
    </div>
    <div class="node-content">
      <img class="node-img-logo" :src="imgCot[logo]?imgCot[logo]:imgCot['dataHandler']" alt=""/>
    </div>
    <div class="node-footer">
      <img class="node-img" :class="loading?'loading':''" :src="imgCot[status]" alt=""/>
    </div>

  </div>
</template>

<script lang="ts">

import {defineComponent, inject} from "vue";

export default defineComponent({

  name: 'NodeCell',
  data() {
    return {
      node:{} as any,
      data: {},
      configType:"",
      logo:"dataHandler",
      status: 'blank',
      loading:false,
      imgCot: {
        blank: require('@/assets/status/blank.png'),
        running: require('@/assets/status/running.png'),
        setting: require('@/assets/status/logo.png'),
        success: require('@/assets/status/success.png'),
        failed: require('@/assets/status/failed.png'),
        loading: require('@/assets/status/running.png'),
        dataHandler: require('@/assets/logo/data_handler.png'),
        db: require('@/assets/logo/db.png'),
        localfile: require('@/assets/logo/localfile.png'),
        kmeans: require('@/assets/logo/KMeans.png'),
        DTC: require('@/assets/logo/分类决策树.png'),
        GBDT: require('@/assets/logo/梯度提升树.png'),
        nullHandler: require('@/assets/logo/缺失值处理.png'),
        dateHandler: require('@/assets/logo/日期.png'),
        datalink: require('@/assets/logo/数据关联.png'),
        normalization: require('@/assets/logo/归一化标准化.png'),
        unrepeated: require('@/assets/logo/删除重复项.png'),
        ARIMA: require('@/assets/logo/ARIMA.png'),
        svm: require('@/assets/logo/SVM支持向量机-选中.png'),
        randomForests: require('@/assets/logo/随机森林回归.png'),
        exception: require('@/assets/logo/异常检测.png'),
        modelTrain: require('@/assets/logo/model_train.png'),
      },
      label: '',
    }
  },
  watch:{
    status: function (n,o){
      if(n=="running"){
        this.loading=true
      }else{
        this.loading=false
      }
    },
  },
  methods: {
    openSetting() {
      const setConfig = (this.$refs.setConfig as any)
      setConfig.openDrawer()
    }
  },
  mounted() {
    const node = (inject('getNode') as any)()
    this.node = node
    if(node.data.data.logoName){
      this.logo = node.data.data.logoName
    }
    this.label = node.data.label
    this.configType = node.data.name

    // 监听数据改变事件
    node.on('change:data', (data: any) => {
      const current = data.current
      this.label = current.label
      this.status = current.status
    })
  },
})
</script>
<style lang="less">
.node {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 100%;
  height: 100%;
  background-color: #fff;
  border: 1px solid #c2c8d5;
  border-left: 4px solid #5F95FF;
  border-radius: 10px;
  box-shadow: 0 2px 5px 1px rgba(0, 0, 0, 0.06);
  &-spin{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    position: absolute;
    border-radius: 8px;
    background-color: rgba(0,0,0);
    opacity:0.5
  }
  &-header {
    display: flex;
    justify-content: center;
  }

  &-content {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-grow: 1
  }

  &-footer {
    display: flex;
    justify-content: space-around;
  }
}

.node-img {
  width: 30px;
  height: 30px;
}

.node-img-setting {
  cursor: pointer;
}

.node-img-logo {
  max-width: 60px;
  width: 70%;
}

.node .label {
  display: inline-block;
  flex-shrink: 0;
  color: #666;
  font-size: 12px;
}

.node .status {
  flex-shrink: 0;
}

.node.success {
  border-left: 4px solid #52c41a;
}

.node.failed {
  border-left: 4px solid #ff4d4f;
}

.loading {
  animation: spin 1s linear infinite;
}

@keyframes running-line {
  to {
    stroke-dashoffset: -1000;
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
