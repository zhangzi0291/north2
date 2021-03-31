<style lang="less">

.box {
  margin: 10px;
  width: 100px;
  height: 100px;
  outline: 3px dashed black;
  animation: 1s animateBorder infinite;
  animation-direction:alternate;
}

@keyframes animateBorder {
  to {
    outline: 3px dashed white;
    box-shadow: 0 0 0 3px black;
  }
}

</style>
<template>
  <div>
    <div class="box">
      box
    </div>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';

@Options({
  name: "test",
  data() {
    return {
    }
  },
  methods: {

  },
  created() {
  }
})
export default class test extends Vue {
}
</script>
