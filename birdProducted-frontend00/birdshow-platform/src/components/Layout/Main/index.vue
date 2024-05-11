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
    default: false
  },
  // 上边距
  marginTop: {
    type: String,
    default: '0'
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
  @media screen and (max-width: 1200px) {
    margin: 0;
  }
  // 过渡效果
  transition: margin 0.5s;
  margin: 0 10%;
  height: 100%;
  display: flex;
  padding-bottom: 2rem;
}

.content_container {
  width: 100%;
  height: 100%;
  transition: padding 0.5s;
  //border: red 1px solid;
  @media screen and (max-width: 910px) {
    padding: 0.2rem;
  }
  padding: 1rem;
  background-color: var(--el-bg-color);
  border-radius: $border-radius;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.information_container {
  width: 30%;
  height: 100%;
  @media screen and (max-width: 910px) {
    display: none;
  }

}


</style>