<style lang="less" scoped>
.layout {
  top: 0;
  left: 0;
  right: 0;
  position: absolute;
  min-height: 100%;
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
      <a-menu v-model:selectedKeys="selectedKeys" mode="inline" theme="dark"
              @click="clickMenu">

        <template v-for="menu in menus">
          <template v-if="menu.child || !menu.data.resourceUrl ">
            <n-menu :key="menu.id" :menu="menu"></n-menu>
          </template>
          <template v-else>
            <a-menu-item :key="menu.data.resourceUrl" :title="menu.data.resourceType">
              <component :is="$icons[menu.data.resourceIcon]"/>
              <span>{{ menu.data.resourceName }}{{ menu.data.pid }}</span>
            </a-menu-item>
          </template>
        </template>

      </a-menu>
    </a-layout-sider>

    <a-layout >

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
          :style="{  'overflow-x':'auto',margin: '24px 16px', padding: '24px', background: '#fff' }"
      >

        <router-view/>

      </a-layout-content>

    </a-layout>

    <change-password ref="cp"></change-password>
    <change-user-info ref="cui"></change-user-info>


  </a-layout>

</template>
<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import nMenu from '@/components/base/menu/n-menu.vue'

import SysResourceApi from '@/api/SysResourceApi'
import BaseApi from '@/api/BaseApi'
import ChangePassword from "@/views/home/ChangePassword.vue";
import ChangeUserInfo from "@/views/home/ChangeUserInfo.vue";
import SysLoginApi from "@/api/SysLoginApi";
import {createVNode} from "vue";
import messages from '@/protobuf/NotificationMessage_pb'

const sysApi = new SysResourceApi()

@Options({
  name: 'Index',
  components: {
    nMenu, ChangePassword, ChangeUserInfo
  },
  data() {
    return {
      logoName: "LOGO",
      selectedKeys: ["/"],
      collapsed: false,
      menus: [],
      reconnectInterval: {},
    }
  },
  computed: {

  },

  methods: {
    clickMenu(item: any) {
      if (item.item.title == 4) {
        this.$router.push({
          path:"/iframePanel",
          query:{
            url:item.key
          }
        });
      } else if (item.item.title == 5) {
        window.open(item.key, '_blank');
      } else {
        this.$router.push(item.key);
      }
    },
    collapse(collapsed: boolean) {
      this.collapsed = collapsed
    },
    openChangePassword() {
      this.$refs.cp.open()
    },
    openChangeUserInfo(){
      this.$refs.cui.open(this.user.userId)
    },
    logout() {
      this.$modal.confirm({
        title: '退出',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要退出吗？',
        onOk: () => {
          return SysLoginApi.logout().then(res => {
            BaseApi.setWsIsClose(true);
            window.websocket.close()
            this.$router.push({path: '/login',})
          })
        },
      });
    },
    createWs() {
      if ('WebSocket' in window) {
        window.websocket = new WebSocket(window.BASE_WS_URL + "/ws");
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
          if(!BaseApi.getWsIsClose()){
            this.createWs()
          }
        }
      } else {
        alert('Not support websocket')
      }
    },
    wsBinaryHandler(msg: any) {
      var reader = new FileReader();
      reader.readAsArrayBuffer(msg.data);
      reader.onload = (e) => {
        var buf = new Uint8Array(<ArrayBuffer>reader.result);
        var notificationMessage = messages.NotificationMessage.deserializeBinary(buf)
        this.openNotification(notificationMessage.getTitle(), notificationMessage.getMessage());
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
    this.selectedKeys = [this.$route.path]
    sysApi.getMenu().then((res) => {
      this.menus = res.data.data
    });
    BaseApi.setWsIsClose(false);
    this.createWs()

  },
})

export default class Index extends Vue {
}

</script>