<style lang="less">
@border-box-width: 200px;
@border-box-height: 200px;
@border-box-padding: 3px;
.border-box {

  width: @border-box-width;
  height: @border-box-height;
  padding: @border-box-padding;
  display: inline-block;
  background: repeating-linear-gradient(135deg, transparent, transparent 3px, #66ccff 3px, #66ccff 8px);
  animation: shine 100s infinite linear;
  overflow: hidden;

  input {
    border: 0;
  }

  @keyframes shine {
    0% {
      background-position: -1px -1px;
    }
    100% {
      background-position: -810px -810px;
    }
  }

  .box {
    display: inline-block;
    width: calc(@border-box-width - 2 * @border-box-padding);
    height: calc(@border-box-height - 2 * @border-box-padding);
    background-color: #fff;
  }
}


.arrow-box {
  width: @border-box-width;
  height: @border-box-height;
  display: inline-block;

  .arrow {

  }
}

</style>
<template>
  <div>
    <div class="box">
      box
    </div>
    <div class="border-box">
      <div class="box">
        <div>1</div>
        <div>5</div>
        <div>2</div>
        <div>3</div>
        <div>4</div>
      </div>
    </div>
    <div class="arrow-box">
      <Icon type="chevron-right"></Icon>
      <Icon type="chevron-right"></Icon>
      <Icon type="chevron-right"></Icon>
      <Icon type="chevron-right"></Icon>
    </div>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';

@Options({
  name: "test",
  data() {
    return {}
  },
  methods: {},
  created() {
  }
})
export default class test extends Vue {
}
</script>
