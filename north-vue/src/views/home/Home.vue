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
      <a-form layout="inline">
        <a-form-item>
          <progress-block :title="'CPU'" :percent="cpuPercent" :strokeColor="getDashboardColor(cpuPercent)" />
        </a-form-item>
        <a-form-item>
          <progress-block :title="'内存'" :percent="memeryPercent" :strokeColor="getDashboardColor(memeryPercent)" />
        </a-form-item>
        <a-form-item v-for="(item,index,i) in hardwareInfo.disk">
          <progress-block :title="'磁盘 '+index" :percent="diskPercent[i]" :strokeColor="getDashboardColor(diskPercent[i])" />
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
import ProgressBlock from "@/views/home/ProgressBlock.vue";
import HomeApi from "@/api/HomeApi";

@Options({
  name: "Home",
  components: {
    InfoBlock,ProgressBlock
  },
  data() {
    return {
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
      ],
      dashboardColor: {
        normal: "#2db7f5",
        warning: "#ff9900",
        danger: "#ed4014",
      },
      hardwareInfo: {
        cpu: {},
        memery: {},
        disk: {},
      },
      totalUser: undefined,
      onlineUser: undefined,
      todyUser: undefined,
      hardwareInterval:{},
    }
  },
  computed: {
    cpuPercent() {
      if (!!this.hardwareInfo.cpu.system) {
        return this.hardwareInfo.cpu.system / 1;
      }
      return 0
    },
    memeryPercent() {
      if (!!this.hardwareInfo.memery.used && !!this.hardwareInfo.memery.total) {
        var p = <number><unknown>(this.hardwareInfo.memery.used.replace('MB', '') / this.hardwareInfo.memery.total.replace('MB', '') * 100).toFixed(2)
        return p / 1;
      }
      return 0
    },
    diskPercent() {
      var list = []
      for (let diskKey in this.hardwareInfo.disk) {
        if (!!this.hardwareInfo.disk[diskKey].used && !!this.hardwareInfo.disk[diskKey].total) {
          var p = <number><unknown>(this.hardwareInfo.disk[diskKey].used.replace('G', '') / this.hardwareInfo.disk[diskKey].total.replace('G', '') * 100).toFixed(2)
          list.push(p / 1)
        }
      }
      return list
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
    },
    getHardwareInfo() {
      HomeApi.getHardwareInfo().then(res => {
        this.hardwareInfo = res.data.data
      })
    },
    getDashboardColor(percent: number) {
      if (percent > 85) {
        return this.dashboardColor.danger
      } else if (percent > 60) {
        return this.dashboardColor.warning
      } else if (percent < 60) {
        return this.dashboardColor.normal
      }
      return this.dashboardColor.normal
    }
  },
  created() {
    this.getTotalUser();
    this.getOnlineUser();
    this.getTodayUser();
    this.getHardwareInfo();
    this.hardwareInterval = setInterval( ()=>{
      this.getHardwareInfo();
    },30000)
  },
  unmounted() {
    clearInterval(this.hardwareInterval)
  }

})
export default class Home extends Vue {
}
</script>
