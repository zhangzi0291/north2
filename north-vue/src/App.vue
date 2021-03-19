<style lang="less">
@import "./main.less";

html, body {
  height: 100%;
  padding: 0;
  margin: 0;
  font-family: "Microsoft YaHei";
}

.top {
  height: 100%;
}

</style>
<template>
  <div class="top">
    <a-config-provider :locale="locale">
      <router-view/>
    </a-config-provider>
  </div>
</template>
<script>
import zhCN from 'ant-design-vue/es/locale/zh_CN';

export default {
  data() {
    return {
      locale: zhCN,
    };
  },
};
</script>

