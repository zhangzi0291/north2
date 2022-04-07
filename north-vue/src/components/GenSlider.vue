<template>
  <div>
    <a-button @click="openModal" block>
      <template v-if="!isValid">
        点击按钮进行验证
      </template>
      <template v-else>
        <CheckOutlined/>
      </template>
    </a-button>
    <a-modal :width="278" :visible="visible" :footer="null" :closable="false" @cancel="hideModal">
      <div class="slider">
        <div class="content">
          <div class="bg-img-div">
            <img id="bg-img" :src="bgImg" alt/>
          </div>
          <div ref="sliderImgDiv" class="slider-img-div" :style="moveBtnStyle">
            <img id="slider-img" :src="sliderImg" alt/>
          </div>
        </div>
        <div class="slider-move">
          <div class="slider-move-track">
            拖动滑块完成拼图
          </div>
          <div class="slider-move-btn" :style="imgDivStyle" @mousedown="down"></div>
        </div>
        <div class="bottom">
          <div class="close-btn" @click="hideModal"></div>
          <div class="refresh-btn" @click="refreshCaptcha"></div>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts">

import {defineComponent} from "vue";
import SysLoginApi from "@/api/SysLoginApi";

export default defineComponent({
  name: "GenSlider",
  data() {
    return {
      isValid: false,
      visible: false,
      bgImgWidth: 260,
      movePercent: 0,
      moveX: 0,
      moveBtnStyle: {},
      imgDivStyle: {},
      bgImg: "",
      sliderImg: "",
      startSlidingTime:0,
      entSlidingTime:0,
      trackArr:[]
    }
  },
  methods: {
    openModal() {
      this.visible = true;
      this.trackArr = []
      this.refreshCaptcha()
    },
    hideModal() {
      this.visible = false;
    },
    refreshCaptcha() {
      SysLoginApi.gen().then(res => {
        this.id = res.data.id;
        this.bgImg = res.data.captcha.backgroundImage;
        this.sliderImg = res.data.captcha.sliderImage;
        this.trackArr = []
      })
    },
    down(event) {
      this.startX = event.pageX;
      this.startY = event.pageY;
      this.startSlidingTime = new Date();
      window.addEventListener("mousemove", this.move);
      window.addEventListener("mouseup", this.up);
    },
    move(event) {
      let moveX = event.pageX - this.startX;
      let moveY = event.pageY - this.startY;
      this.moveX = moveX
      this.moveBtnStyle = {
        transform: "translate(" + this.moveX + "px, 0px)"
      }
      this.imgDivStyle = {
        transform: "translate(" + this.moveX + "px, 0px)"
      }
      this.movePercent = moveX / this.bgImgWidth;
      this.trackArr.push({x: moveX, y:moveY, t: (new Date().getTime() - this.startSlidingTime.getTime())});

    },
    up(event) {
      console.log(this.movePercent, this.bgImgWidth);
      this.entSlidingTime = new Date()
      this.moveX = 0
      this.moveBtnStyle = {
        transform: "translate(" + this.moveX + "px, 0px)"
      }
      this.imgDivStyle = {
        transform: "translate(" + this.moveX + "px, 0px)"
      }
      window.removeEventListener("mousemove", this.move);
      window.removeEventListener("mouseup", this.up);
      this.valid();
    },
    valid() {
      let data = {
        bgImageWidth: this.bgImgWidth,
        bgImageHeight: this.$refs.sliderImgDiv.offsetHeight,
        sliderImageWidth: this.$refs.sliderImgDiv.offsetWidth,
        sliderImageHeight: this.$refs.sliderImgDiv.offsetHeight,
        startSlidingTime: this.startSlidingTime,
        entSlidingTime: this.entSlidingTime,
        trackList: this.trackArr
      };
      SysLoginApi.check(data).then(res => {
        if (eval(res.data.data)) {
          this.hideModal()
          this.$emit("valid", this.id)
          this.isValid = true
        } else {
          this.$emit("valid", false)
          this.isValid = false
          this.$message.error("验证失败");
          this.refreshCaptcha()
        }
      })
    },
  },
})
</script>

<style lang="less">
.ant-modal-body {
  padding: 0;
}

.slider {
  background-color: #fff;
  width: 278px;
  height: 285px;
  z-index: 999;
  box-sizing: border-box;
  padding: 9px;
  border-radius: 6px;
  box-shadow: 0 0 11px 0 #999999;
}

.slider .content {
  width: 100%;
  height: 159px;
  position: relative;
}

.bg-img-div,
.slider-img-div {
  width: 100%;
  height: 100%;
  position: absolute;
  transform: translate(0px, 0px);
}

.bg-img-div img {
  width: 100%;
}

.slider-img-div img {
  height: 100%;
}

.slider .slider-move {
  height: 60px;
  width: 100%;
  margin: 11px 0;
  position: relative;
}

.slider .bottom {
  height: 19px;
  width: 100%;
}

.refresh-btn, .close-btn, .slider-move-track, .slider-move-btn {
  background:  url('~@/assets/images/sprite.1.2.4.png') no-repeat;
}

.refresh-btn, .close-btn {
  display: inline-block;
}

.slider-move .slider-move-track {
  line-height: 38px;
  font-size: 14px;
  text-align: center;
  white-space: nowrap;
  color: #88949d;
  -moz-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}

.slider {
  user-select: none;
}

.slider-move .slider-move-btn {
  transform: translate(0px, 0px);
  background-position: -5px 11.79625%;
  position: absolute;
  top: -12px;
  left: 0;
  width: 66px;
  height: 66px;
}

.slider-move-btn:hover, .close-btn:hover, .refresh-btn:hover {
  cursor: pointer
}

.bottom .close-btn {
  width: 20px;
  height: 20px;
  background-position: 0 44.86874%;
  padding: 0 20px;
}

.bottom .refresh-btn {
  width: 20px;
  height: 20px;
  background-position: 0 81.38425%;
}
</style>