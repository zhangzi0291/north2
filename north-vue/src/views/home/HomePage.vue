<style lang="less">

</style>
<template>
  <a-form :layout="'inline'">
    <a-form-item>
      <info-block :icon="'UserOutlined'" :number="totalUser" :title="'总用户数'"/>
    </a-form-item>
    <a-form-item>
      <info-block :icon="'UserOutlined'" :number="onlineUser" :title="'当前在线'"/>
    </a-form-item>
    <a-form-item>
      <info-block :icon="'UserOutlined'" :number="todayUser" :title="'今日登录'"/>
    </a-form-item>
  </a-form>
  <a-form :layout="'inline'">
    <a-form-item>
      <progress-block :percent="cpuPercent" :title="'CPU'"/>
    </a-form-item>
    <a-form-item>
      <progress-block :percent="memeryPercent" :title="'内存'"/>
    </a-form-item>
    <a-form-item v-for="(item,index) in hardwareInfo.disk">
      <progress-block :percent="diskPercent[index]" :title="'磁盘 '+index"/>
    </a-form-item>
  </a-form>

</template>

<script lang="ts">
import InfoBlock from "@/views/home/InfoBlock.vue";
import ProgressBlock from "@/views/home/ProgressBlock.vue";
import {defineComponent} from "vue";
import {HomeInfo} from "@/proto/Protobuf";
import HomeApi from "@/api/HomeApi";

export default defineComponent({
  name: "Home",
  components: {
    InfoBlock, ProgressBlock
  },
  data() {
    return {
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
      ],
      hardwareInfo: <any>{
        cpu: <any>{},
        memery: <any>{},
        disk: <any>{},
      },
      totalUser: undefined,
      onlineUser: undefined,
      todayUser: undefined,
      hardwareInterval: <any>{},
      reconnectInterval: {},
      socket: <any>{},
    }
  },
  computed: {
    cpuPercent() {
      if (this.hardwareInfo.cpu.system) {
        let p = (this.hardwareInfo.cpu.system / 1).toFixed(2)
        return Number(p)
      }
      return 0
    },
    memeryPercent() {
      if (this.hardwareInfo.memery.used && this.hardwareInfo.memery.total) {
        let p = (this.hardwareInfo.memery.used.replace('MB', '') / this.hardwareInfo.memery.total.replace('MB', '') * 100).toFixed(2)
        return Number(p)
      }
      return 0
    },
    diskPercent() {
      let list = []
      if (this.hardwareInfo.disk.length > 0) {
        for (let diskKey in this.hardwareInfo.disk) {
          let p = (this.hardwareInfo.disk[diskKey].used.replace('G', '') / this.hardwareInfo.disk[diskKey].total.replace('G', '') * 100).toFixed(2)
          list.push(Number(p))
        }
      }
      return list
    }
  },
  methods: {
    createWs() {
      if ('WebSocket' in window) {
        this.socket = new WebSocket(window.BASE_WS_URL + "/hardwareInfo");
        this.socket.onmessage = (msg: any) => {
          if (msg.data instanceof Blob) {
            this.wsBinaryHandler(msg)
          } else {
          }
        };
        this.socket.onopen = () => {
          console.log("连接成功")
        }
      } else {
        alert('Not support websocket')
      }
    },
    wsBinaryHandler(msg: any) {
      let reader = new FileReader();
      reader.readAsArrayBuffer(msg.data);
      reader.onload = () => {
        let buf = new Uint8Array(<ArrayBuffer>reader.result);
        let homeInfo = HomeInfo.decode(buf)
        this.totalUser = homeInfo.totalUser
        this.todayUser = homeInfo.todayUser
        this.onlineUser = homeInfo.onlineUser
        this.onlineUser = homeInfo.onlineUser
        this.hardwareInfo.cpu = homeInfo.cpu
        this.hardwareInfo.memery = homeInfo.memery
        this.hardwareInfo.disk = homeInfo.disk
      }
    },
  },
  created() {
    this.createWs()
    HomeApi.getTotalUser().then((res) => {
      console.log(res.data)
    })
  },
  unmounted() {
    this.socket.close()
  }

})
</script>
