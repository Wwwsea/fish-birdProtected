<script setup lang="ts">

const noDisplayComponent = computed(() => {
  // 不显示底部
  if (useRoute().path == '/chat-gpt') {
    return false
  } else return useRoute().path != '/chat-gpt1';
})

</script>

<template>
  <Header/>
  <div style="min-height: 100vh">
    <router-view v-slot="{Component}">
      <transition name="el-fade-in-linear" mode="out-in">
        <component :is="Component"/>
      </transition>
    </router-view>
  </div>
  <template v-if="noDisplayComponent">
    <Footer/>
  </template>
  <ToTop/>
</template>

<style scoped lang="scss">

</style>