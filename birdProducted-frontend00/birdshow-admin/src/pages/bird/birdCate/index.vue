<script setup lang="ts">
import type {Ref, UnwrapRef} from 'vue'
import {h} from 'vue'
import type {TableColumnsType} from 'ant-design-vue'
import { birdcategoriesList, searchCategoryById, searchCategory, deleteCategoryByIds } from '~/api/bird/birdCate'
import type {CateDate} from '~/layouts/basic-layout/typing.ts'
import {CateDataItem} from "~/pages/bird/birdCate/type.ts";
import modal from './modal.vue'
import {message} from "ant-design-vue";
import dayjs from "dayjs";

defineExpose({h})

// 是否加载中
const isLoading = ref<boolean>(true)
// 打开对话框
const modalOpen = ref<boolean>(false)
// 对话框标题
const modalTitle = ref<string>()

const formState = reactive({
  categoryName: '',
  biologyBranch: undefined,
  time: undefined
})


async function onFinish(values: any) {
// +++++
  const submitData = {
    ...values,
    startTime: values.time && values.time.length > 0 ? dayjs(values.time[0]).format('YYYY-MM-DD HH:mm:ss') : undefined,
    endTime: values.time && values.time.length > 0 ? dayjs(values.time[1]).format('YYYY-MM-DD HH:mm:ss') : undefined,
  }

  isLoading.value = true
  console.log("submitData----",submitData)
  const { data } = await searchCategory(submitData)
  console.log("data-----",data)
  cateDataList.value = data as any
  newCateData.value = buildTree(data)
  isLoading.value = false
  // refreshFunc()

}

function onFinishFailed(errorInfo: any) {
  console.log('出现错误:', errorInfo)
}

const cateColumns = ref<TableColumnsType>([
  {
    title: '分类名',
    dataIndex: 'categoryName',
    width: '10%',
    align: 'center',
  },
  {
    title: '分类下文章数量',
    dataIndex: 'articleCount',
    width: '10%',
    align: 'center',
  },
  {
    title: '生物分支',
    dataIndex: 'biologyBranch',
    width: '10%',
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: '10%',
    align: 'center',
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    width: '10%',
    align: 'center',
  },
  {
    title: '操作',
    key: 'operation',
    width: '10%',
    align: 'center',
  }
])

const getButtonColor = (branch) => {
  switch (branch) {
    case '目':
      return 'red';
    case '科':
      return 'yellow';
    case '属':
      return 'green';
    default:
      return '';
  }
};

onMounted(() => {
  getCateList()
})

// 元数据
const cateDate = ref()

// 构建好的树形数据
const cateDataList: Ref<UnwrapRef<CateDate>> = ref([])
// 上级分类数据
const newCateData = ref()

/**
 * 获取分类列表
 */
async function getCateList() {
  const {data} = await birdcategoriesList() as any
  cateDate.value = data
  cateDataList.value = buildTree(data)
  console.log("cateDataList.value::",cateDataList.value)
  newCateData.value = buildTree(data)
  isLoading.value = false
}


/**
 * 构建树形数据
 * @param data 原始数据
 */
function buildTree(data: CateDataItem[]) {
  let tree = data.filter(item => item.parentId === null || item.parentId === 0);
  tree.forEach(root => {
    root.key = root.id
    let children = buildChildren(root, data);
    if (children.length > 0) {
      root.children = children;
    }
  });
  return tree;
}

/**
 * 构建子节点
 * @param parent 父节点
 * @param data 原始数据
 */
function buildChildren(parent: CateDataItem, data: CateDataItem[]) {
  let children = data.filter(item => item.parentId === parent.id);
  children.forEach(child => {
    child.key = child.id
    let grandChildren = buildChildren(child, data);
    if (grandChildren.length > 0) {
      child.children = grandChildren;
    }
  });
  return children;
}

// 展开的行
const expand = ref({
  expandedRowKeys: <any>([]),
  flag: false})

function onExpandAll() {
  if (expand.value.flag) {
    expand.value.expandedRowKeys = []
    expand.value.flag = false
  } else {
    expand.value.expandedRowKeys = cateDate.value.map(item => item.id)
    expand.value.flag = true
  }
}

/**
 * 展开行
 * @param expanded 是否展开
 * @param record 当前行数据
 */
function handleExpand(expanded: boolean, record: any) {
  if (expanded) {
    expand.value.expandedRowKeys.push(record.key);
  } else {
    // 是否存在
    const index = expand.value.expandedRowKeys.indexOf(record.key);
    if (index > -1) {
      // 删除
      expand.value.expandedRowKeys.splice(index, 1);
    }
  }
}

/**
 * 刷新
 */
function refreshFunc() {
  isLoading.value = true
  getCateList()
}

const showModal = (flag: string) => {
  if (flag === 'add') {
    modalType.value = 0
    modalTitle.value = '添加分类'
  } else if (flag === 'update') {
    modalType.value = 1
    modalTitle.value = '修改分类'
  }

  modalOpen.value = true;
}

/**
 * 取消或确定回调
 * @param e
 */
function handleModalOpenUpdate(e: boolean) {
  modalOpen.value = e
  refreshFunc()
}

/**
 * 添加成功回调
 */
function handleAddSuccess() {
  refreshFunc()
}



const formData = ref()
// 弹窗标识：0：新增 1：修改 2：删除
const modalType: Ref<UnwrapRef<number | null >> = ref(null)

/**
 * 修改
 */
async function updateCate(id: string) {
  const {data} = await searchCategoryById(id);
  formData.value = data
  console.log("update----formData.value+++++:",formData.value)
  showModal('update')
}

/**
 * 添加
 */
function addCate(flag: string, parentId?: string) {
  const form = {
    // id
    id: '',
    // 上级分类
    parentId: '',
    // 分类标题
    categoryName: '',
    // 生物分支
    biologyBranch: '',
  }
  if (parentId !== undefined || !parentId.equal('0')) {
    form.parentId = parentId
  }
  console.log("add----formData.value+++++:",formData.value)
  formData.value = form
  showModal(flag)
}

/**
 * 删除 √
 */
async function deleteCate (id: string) {
  const data = await deleteCategoryByIds(id).catch(res => {
    message.warn(res)
  })
  if (data.code === 200) {
    message.success('分类删除成功')
    refreshFunc()
  }
}

</script>

<template>
  <layout
      @update:refreshFunc="refreshFunc"
      @update:onFinish="onFinish"
      @update:onFinishFailed="onFinishFailed"
      :formState="formState"
  >
    <template #form-items>
      <a-form-item
          label="分类名称"
          name="categoryName"
      >
        <a-input v-model:value="formState.categoryName" placeholder="请输入分类名称"/>
      </a-form-item>

      <a-form-item label="生物分支" name="biologyBranch" style="width: 240px">
        <a-select v-model:value="formState.biologyBranch" placeholder="请选择生物分支">
          <a-select-option :value=" '目' ">
            目
          </a-select-option>
          <a-select-option :value=" '科'">
            科
          </a-select-option>
          <a-select-option :value=" '属' ">
            属
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item
          label="创建时间"
          name="time"
      >
        <a-range-picker v-model:value="formState.time" :placeholder="['开始时间', '结束时间']" />
      </a-form-item>
    </template>

    <template #operate-btn>
      <div>
        <a-button @click="addCate('add')" type="primary">
          <template #icon>
            <PlusOutlined/>
          </template>
          新增
        </a-button>
        <a-button type="dashed" style="margin-bottom: 10px;color: grey" @click="onExpandAll">
          <template #icon>
            <ArrowsAltOutlined/>
          </template>
          展开/折叠
        </a-button>
      </div>
    </template>
    <template #table-content>
      <div>
        <a-table
            :columns="cateColumns"
            :data-source="cateDataList"
            :expanded-row-keys="expand.expandedRowKeys"
            @expand="handleExpand"
            :loading="isLoading"
        >
          <template #bodyCell="{ column,record }">

            <template v-if="column.dataIndex === 'biologyBranch'">
              <template v-if="record.biologyBranch.length <= 2">
                <a-tag v-for="branch in record.biologyBranch" :key="branch" :color = "getButtonColor(branch)" :bordered="false">
                  {{ branch }}
                </a-tag>
              </template>
            </template>

<!--            <template v-if="column.key === 'biologyBranch'">
              <span :class="{ 'red-text': record.biologyBranch === '科' }">{{ record.biologyBranch }}</span>
&lt;!&ndash;              <a-button :style="{ backgroundColor: getButtonColor(record.biologyBranch) }"> {{ record.biologyBranch }} </a-button>&ndash;&gt;
              <a-button type="primary"> uuuu </a-button>
            </template>-->

            <template v-if="column.key === 'operation'">
              <a-button type="link" style="padding: 0;" @click="updateCate(record.id)">
                <template #icon>
                  <FileSyncOutlined/>
                </template>
                <span style="margin-inline-start:1px">修改</span>
              </a-button>
              <a-button type="link" style="padding: 0;margin-left: 5px" @click="addCate('add',record.parentId)">
                <template #icon>
                  <PlusOutlined/>
                </template>
                <span style="margin-inline-start:1px">新增</span>
              </a-button>
              <a-popconfirm
                  title="是否确定删除"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="deleteCate(record.id)"
              >
                <a-button type="link" style="padding: 0;margin-left: 5px">
                  <template #icon>
                    <DeleteOutlined/>
                  </template>
                  <span style="margin-inline-start:1px">删除</span>
                </a-button>
              </a-popconfirm>
            </template>
          </template>
        </a-table>
      </div>
      <template v-if="cateDataList.length > 0">
        <modal :modalOpen="modalOpen"
               :modalTitle="modalTitle"
               @update:modalOpen="handleModalOpenUpdate"
               @add:success="handleAddSuccess"
               :cateData="newCateData"
               :formData="formData"
               :modalType="modalType"
        />
      </template>
    </template>
  </layout>
</template>

<style scoped lang="less">
.middle_btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.red-text {
  color: red;
}
</style>
