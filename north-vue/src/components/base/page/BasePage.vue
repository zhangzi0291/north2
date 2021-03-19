<style lang="less" scoped>

</style>
<template>
  <div>
    <slot name="breadcrumb">
      <a-page-header>
        <template #title>
          <a-breadcrumb>
            <a-breadcrumb-item v-for="breadcrumb in breadcrumbs" :href="breadcrumb.href" :key="breadcrumb.name">
              <a :href="'#'+breadcrumb.href">
                <component :is="$icons[breadcrumb.icon]"/>
                <span>{{ breadcrumb.name }}</span>
              </a>
            </a-breadcrumb-item>
          </a-breadcrumb>
        </template>
      </a-page-header>
    </slot>
    <a-card>
      <slot name="content">
      </slot>
    </a-card>

  </div>
</template>
<script lang="ts">
import {Options, Vue} from 'vue-class-component';

@Options({
  name: 'base-page',
  data() {
    return {
      routes: []
    }
  },
  props: {
    breadcrumbs: {
      type: Array,
    }
  },
  methods: {
    clickBreadcrumb(e: any) {
      console.log(e);
    },
    back() {
      this.$router.go(-1)
    },

  },
  mounted() {

  }
})

export default class BasePage extends Vue {
}

</script>