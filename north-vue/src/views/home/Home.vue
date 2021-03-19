<style lang="less">

</style>
<template>
  <base-page :breadcrumbs="breadcrumbs">
    <template #content>
      <a-form layout="inline">
        <a-form-item>
          <info-block :icon="'UserOutlined'" :number="totalUser" :title="'总用户数'"/>
        </a-form-item>
        <a-form-item>
          <info-block :icon="'UserOutlined'" :number="onlineUser" :title="'当前在线'"/>
        </a-form-item>
        <a-form-item>
          <info-block :icon="'UserOutlined'" :number="todyUser" :title="'今日登录'"/>
        </a-form-item>
      </a-form>

      <a-card>
      </a-card>
    </template>
  </base-page>

</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import InfoBlock from "@/views/home/InfoBlock.vue";
import HomeApi from "@/api/HomeApi";

@Options({
  name: "Home",
  components: {
    InfoBlock
  },
  data() {
    return {
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
      ],
      totalUser: undefined,
      onlineUser: undefined,
      todyUser: undefined,
    }
  },
  methods: {
    getTotalUser() {
      HomeApi.getTotalUser().then(res => {
        this.totalUser = res.data.data
      })
    },
    getOnlineUser() {
      HomeApi.getOnlineUser().then(res => {
        this.onlineUser = res.data.data
      })
    },
    getTodayUser() {
      HomeApi.getTodayUser().then(res => {
        this.todyUser = res.data.data
      })
    }
  },
  created() {
    this.getTotalUser();
    this.getOnlineUser();
    this.getTodayUser();

  }
})
export default class Home extends Vue {
}
</script>
