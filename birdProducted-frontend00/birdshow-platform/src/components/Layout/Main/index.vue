<script setup lang="ts">
defineProps({
  // 内容区是否只需要父容器
  onlyFatherContainer: {
    type: Boolean,
    default: false
  },
  // 是否有侧边栏
  isSideBar: {
    type: Boolean,
    default: true
  },
  // 上边距
  marginTop: {
    type: String,
    default: '10'
  }
})
</script>

<template>

  <div class="header">
    <slot name="header"/>
    
  </div>

  <div class="div_container" :style="'margin-top:'+marginTop" >
    <!-- 内容区 -->
    <div class="content_container">
      <slot name="content"/>
    </div>
    <!-- 侧边栏 -->
    <div class="information_container" v-if="isSideBar">
      <slot name="information"/>
    </div>
  </div>
  <div>
    <slot name="footer"/>
  </div>
</template>

<style scoped lang="scss">

.div_container {
  padding: 1rem;
  @media screen and (max-width: 2800px) {
    margin: 0;
  }
  // 过渡效果
  transition: margin 0.5s;
  margin: 0 10%;
  height: 100%;
  display: flex;
  flex-direction: row; /* 横向布局 */
  justify-content: center; /* 主轴居中对齐 */
  align-items: flex-start; /* 侧轴顶部对齐 */
  padding-bottom: 2rem;
}

.content_container {
  margin-left: 0em;
  width: 80%;
  height: 100%;
  //width: 70%; /* 占据 70% 的宽度 */
  transition: padding 0.5s;
  //border: red 1px solid;
  @media screen and (max-width: 910px) {
    padding: 0.2rem;
  }
  padding: 1.8rem;
  background-color: var(--el-bg-color);
  border-radius: $border-radius;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.information_container {
  //margin-left: 2em;
  width: 15%;
  height: 100%;
  @media screen and (max-width: 100px) {
    display: none;
  }

}


</style>