<style lang="less" scoped>
.layout {
  top: 0;
  left: 0;
  right: 0;
  position: absolute;
  min-height: 100%;
  max-height: 100%;
  min-width: 720px;
}

.logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
  line-height: 32px;
}

.trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

.trigger:hover {
  color: #1890ff;
}

.header {
  &-left {
    display: inline-block;
    width: 100px;
  }

  &-right {
    display: inline-block;
    padding: 0 20px;
    width: calc(100% - 100px);

    &-div {
      display: flex;
      flex-direction: row-reverse;

    }
  }


}
</style>
<template>
  <a-layout class="layout">

    <a-layout-sider :collapsed="collapsed"
                    @collapse="collapse">
      <div class="logo">
        {{ logoName }}
      </div>
      <a-menu :mode="'inline'"
              v-model:openKeys="openKeys"
              v-model:selectedKeys="selectedKeys"
              theme="dark" @click="clickMenu">
        <template v-for="menu in menus">
          <template v-if="menu.child || !menu.data.resourceUrl ">
            <n-menu :key="menu.id" :menu="menu"></n-menu>
          </template>
          <template v-else>
            <a-menu-item :key="menu.data.resourceUrl" :title="menu.data.resourceType+''">
              <component :is="$icons[menu.data.resourceIcon]"/>
              <span>{{ menu.data.resourceName }}{{ menu.data.pid }}</span>
            </a-menu-item>
          </template>
        </template>

      </a-menu>
    </a-layout-sider>

    <a-layout>

      <a-layout-header style="background: #fff; padding: 0">
        <div>
          <span class="header-left" @click="() => (collapsed = !collapsed)">
            <menu-unfold-outlined v-if="collapsed" class="trigger"/>
            <menu-fold-outlined v-else class="trigger"/>
          </span>
          <span class="header-right">
            <div class="header-right-div">
              <a-space>
                <a-dropdown>
                  <a-avatar :src="user.iconUrl" size="large" style="border: 1px solid #fff;background-color: #66ccff">
                    <template v-if="1==1">{{ user.nickname }}</template>
                  </a-avatar>
                  <template #overlay>

                    <a-menu>
                      <a-menu-item>
                      用户名：{{ user.nickname }}
                      </a-menu-item>
                      <a-menu-divider/>
                      <a-menu-item @click="openChangeUserInfo()">
                        <a-button shape="circle" type="link">
                          个人信息
                          <template #icon>
                            <LockOutlined/>
                          </template>
                        </a-button>
                      </a-menu-item>
                      <a-menu-item @click="openChangePassword()">
                        <a-button shape="circle" type="link">
                          修改密码
                          <template #icon>
                            <LockOutlined/>
                          </template>
                        </a-button>
                      </a-menu-item>
                      <a-menu-item @click="logout()">
                        <a-button shape="circle" type="link">
                          注销
                          <template #icon>
                            <LogoutOutlined/>
                          </template>
                        </a-button>
                      </a-menu-item>
                    </a-menu>

                  </template>
                </a-dropdown>
              </a-space>
            </div>
          </span>
        </div>

      </a-layout-header>

      <a-layout-content
          :style="{  'overflow-x':'auto',padding: '24px', background: '#fff' }"
      >
        <div style="width: 100%;height:calc( 100% - 64px);">
          <!--          <a-page-header>-->
          <!--            <template #title>-->
          <!--              <a-breadcrumb>-->
          <!--                <a-breadcrumb-item v-for="breadcrumb in breadcrumbs" :href="breadcrumb.href" :key="breadcrumb.name">-->
          <!--                  <a :href="'#'+breadcrumb.href">-->
          <!--                    <component :is="$icons[breadcrumb.icon]"/>-->
          <!--                    <span>{{ breadcrumb.name }}</span>-->
          <!--                  </a>-->
          <!--                </a-breadcrumb-item>-->
          <!--              </a-breadcrumb>-->
          <!--            </template>-->
          <!--          </a-page-header>-->
          <a-card :bodyStyle="{height:'100%'}" style="width: 100%;height: 100%;">
            <router-view @breadcrumbs="setBreadcrumbs"/>
          </a-card>

        </div>

      </a-layout-content>

    </a-layout>

    <change-password ref="cp"></change-password>
    <change-user-info ref="cui"></change-user-info>


  </a-layout>

</template>
<script lang="ts">
import nMenu from '@/components/base/menu/n-menu.vue'
import ChangePassword from "@/views/home/ChangePassword.vue";
import ChangeUserInfo from "@/views/home/ChangeUserInfo.vue";
import {createVNode, defineComponent} from "vue";
import {NotificationMessage} from "@/proto/Protobuf";
import BaseApi from "@/api/BaseApi";
import SysLoginApi from "@/api/sys/SysLoginApi";
import SysResourceApi from "@/api/sys/SysResourceApi";

export default defineComponent({
  name: 'Index',
  components: {
    nMenu, ChangePassword, ChangeUserInfo
  },
  data() {
    return {
      logoName: "LOGO",
      selectedKeys: this.$store.state.menuInfo.selectedKeys,
      openKeys:this.$store.state.menuInfo.openKeys,
      collapsed: false,
      menus: [],
      reconnectInterval: {},
      breadcrumbs: [],
    }
  },
  computed: {},
  watch:{
    selectedKeys:function(nv,ov){
      this.$store.commit("setMenuInfo",{
        selectedKeys:nv
      })
    },
    openKeys:function(nv,ov){
      this.$store.commit("setMenuInfo",{
        openKeys:nv
      })
    }
  },
  methods: {
    setBreadcrumbs(breadcrumbs) {
      this.breadcrumbs = breadcrumbs
    },
    clickMenu(item: any) {
      // console.log(item)
      if (item.item.type == 4) {
        this.$router.push({
          path: "/iframePanel",
          query: {
            url: item.key
          }
        });
      } else if (item.item.type == 5) {
        window.open(item.key, '_blank');
      } else {
        this.$router.push(item.key);
      }
    },
    collapse(collapsed: boolean) {
      this.collapsed = collapsed
    },
    openChangePassword() {
      const cp: any = this.$refs.cp
      cp.open()
    },
    openChangeUserInfo() {
      const cui: any = this.$refs.cui
      //@ts-ignore
      const user: any = this.user
      cui.open(user.userId)
    },
    logout() {
      this.$modal.confirm({
        title: '退出',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要退出吗？',
        onOk: () => {
          return SysLoginApi.logout().then(() => {
            BaseApi.setWsIsClose(true);
            window.websocket.close()
            this.$router.push({path: '/login',})
          })
        },
      });
    },
    createWs() {
      if ('WebSocket' in window) {
        window.websocket = new WebSocket(window.BASE_WS_URL + "/notificationMessage");
        window.websocket.onmessage = (msg: any) => {
          if (msg.data instanceof Blob) {
            this.wsBinaryHandler(msg)
          } else {
            this.wsMessageHandler(msg)
          }
        };
        window.websocket.onopen = () => {
          console.log("连接成功")
        }
        this.reconnectInterval = window.websocket.onclose = () => {
          if (!BaseApi.getWsIsClose()) {
            this.createWs()
          }
        }
      } else {
        alert('Not support websocket')
      }
    },
    wsBinaryHandler(msg: any) {
      const reader = new FileReader();
      reader.readAsArrayBuffer(msg.data);
      reader.onload = () => {
        const buf = new Uint8Array(<ArrayBuffer>reader.result);
        const notificationMessage = NotificationMessage.decode(buf)
        this.openNotification(notificationMessage.title, notificationMessage.message);
      }
    },
    wsMessageHandler(msg: any) {
      this.openNotification("msg title", "msg content");
    },
    openNotification(title: string, message: string) {
      this.$notification.open({
        message: title,
        description: message,
        onClick: () => {
          console.log('Notification Clicked!');
        },
      });
    }
  },
  created() {

    SysResourceApi.getMenu().then((res) => {
      this.menus = res.data.data
    });
    BaseApi.setWsIsClose(false);
    this.createWs()

  },
})
</script>