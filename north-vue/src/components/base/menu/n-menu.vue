<template>
  <a-sub-menu :key="menu.data.id" :expandIcon="menu.data.resourceIcon">
    <template #title>
      <span>
        <component :is="$icons[menu.data.resourceIcon]"/>
        <span>{{ menu.data.resourceName }}</span>
      </span>
    </template>
    <template v-for="child in menu.child">
      <template v-if="child.child || !child.data.resourceUrl">
        <n-menu :menu="child" :key="child.id"/>
      </template>
      <template v-else>
        <a-menu-item :key="child.data.resourceUrl" :title="child.data.resourceType" >
          <component :is="$icons[child.data.resourceIcon]"/>
          <span>{{ child.data.resourceName }}</span>
        </a-menu-item>
      </template>

    </template>
  </a-sub-menu>
</template>
<script lang="ts">
export default ({
  name: "n-menu",
  props: {
    menu: {
      type: Object,
    }
  },
  mounted() {

  }
})
</script>