<script setup lang="ts">
const emit = defineEmits(['update:open'])
const prefixCls = shallowRef('ant-pro-drawer-setting')

// 使用 ref 来保存 open 的状态，并初始化为 true
const open = shallowRef(true)

// 点击处理函数，用于切换 open 的状态
function handleVisible() {
  open.value = !open.value
  emit('update:open', !open)
}

const { token } = useAntdToken()
</script>

<template>
  <div
    :class="`${prefixCls}-handle`"
    :style="{
      backgroundColor: token?.colorPrimary,
      borderEndStartRadius: `${token?.borderRadius}px`,
      borderStartStartRadius: `${token?.borderRadius}px`,
    }"
    @click="handleVisible(open)"
  >
  </div>
  <a-drawer :open="!open" :width="300" placement="right" :closable="true" @update:open="handleVisible">
    <span style="color: #2281df">啥也么有. 别点了</span>
  </a-drawer>
</template>

<style lang="less">
@import './index.less';
</style>
