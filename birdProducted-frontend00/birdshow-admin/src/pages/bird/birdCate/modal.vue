<script setup lang="ts">
import type { PropType } from 'vue'
import { ref } from 'vue'
import {type CascaderProps, message} from 'ant-design-vue'
import type { CateDataItem, formType } from '~/pages/bird/birdCate/type.ts'
import { addCategory, updateCategory } from '~/api/bird/birdCate'

const props = defineProps({
  modalOpen: Boolean,
  modalTitle: String,
  cateData: Array as PropType<CateDataItem[]>,
  formData: Object as PropType<formType> | null,
  modalType: Number,
})

const emit = defineEmits(['update:modalOpen', 'add:success'])
const options = ref<CascaderProps['options']>([])

// const branchData={[{ title: '目', value: '目', key: '1' },{ title: '科', value: '科', key: '2' },{ title: '属', value: '属', key: '3' }]}


// 类型是否为目录
const isDir = ref(false)

let viewForm = reactive<formType>({} as formType)

// 监听props.formData变化
watch(() => props.formData, (val) => {
  if (val)
    viewForm = val as any
})

const treeData = computed(() => {
  console.log('hahahahah')
  console.log('props.cateData::::', props.cateData)
  console.log('props.modalTitle::::', props.modalTitle)
  // options.value =
  return convertDataStructure(props.cateData as CateDataItem[])
})

// 更改数据结构
function convertDataStructure(data: any) {
  console.log('数据结构变化前：：', data)
  return data.map((item: any) => {
    let newItem

    newItem = newItem = {
      value: item.id as number,
      label: item.categoryName as string,
      children: [] as any[],
    }

    if (item.children && item.children.length > 0)
      newItem.children = convertDataStructure(item.children)

    return newItem
  })
}

const open = computed(() => {
  return props.modalOpen
})

function handleClose() {
  emit('update:modalOpen', false)
}

async function handleOpen() {
  if (props.modalType === 0) {
    console.log('add的viewForm-----------', viewForm)
    const data = await addCategory(viewForm)
    if (data.code === 200)
      message.success('分类添加成功')
    // eslint-disable-next-line vue/custom-event-name-casing
    emit('add:success')
  }
  else if (props.modalType === 1) {
    const data = await updateCategory(viewForm)
    if (data.code === 200)
      message.success('分类修改成功')
    isDir.value = false
  }
  emit('update:modalOpen', false)
}
</script>

<template>
  <div>
    <a-modal v-model:open="open" :title="modalTitle" @ok="handleOpen" @cancel="handleClose">
      <a-form>
        <a-form-item style="margin-top: 2rem">
          <template #label>
            <a-tooltip placement="top">
              <template #title>
                <span style="font-size: 0.9em">
                  不选择即表示父级菜单
                </span>
              </template>
              <span>上级菜单</span>
            </a-tooltip>
          </template>
          <a-tree-select
            v-model:value="viewForm.parentId"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择上级菜单"
            allow-clear
            :tree-default-expand-all="false"
            :tree-data="treeData"
            tree-node-filter-prop="label"
          />
        </a-form-item>
        <div style="display: flex">
          <a-form-item label="分类名称" style="margin-right: 1em">
            <a-input v-model:value="viewForm.categoryName"/>
          </a-form-item>

        </div>
        <div style="display: flex">

          <a-form-item label="所属分支名称" style="margin-right: 1em">
<!--            <a-input v-model:value="viewForm.biologyBranch"/>-->
            <a-select
              v-model:value="viewForm.biologyBranch"
              placeholder="请选择所属分支名称"
              style="width: 160px"
            >
              <a-select-option value="目">目</a-select-option>
              <a-select-option value="科">科</a-select-option>
              <a-select-option value="属">属</a-select-option>
            </a-select>
          </a-form-item>

        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped lang="less">
.left_right {
  display: flex;
  justify-content: space-between;
}

.icon {
  width: 25em;
  height: 10em;
  overflow-y: scroll;
  display: flex;
  flex-wrap: wrap;

  div {
    margin: 0.1rem;
    padding: 0.1rem;
    cursor: pointer;

    &:hover {
      background: #bdbdbd;
    }
  }
}
</style>
