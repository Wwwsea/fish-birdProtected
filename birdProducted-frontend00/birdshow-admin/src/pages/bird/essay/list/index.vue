<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { createVNode, ref } from 'vue'
import { type CascaderProps, Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import type { CategoryTypeByTree, TagType } from '~/pages/bird/essay/publish/type.ts'
import {
  articleCategoryByTree,
  articleList,
  articleSearch,
  articleTag,
  deleteArticle,
  updateArticleStatus,
  updateArticleTop,
} from '~/api/bird/article'

const formData = ref({
  categoryId: undefined,
  categoryName: undefined,
  tagId: undefined,
  articleTitle: undefined,
  articleType: undefined,
  isTop: undefined,
  status: undefined,
})

// 分类
const birdCateList: Ref<UnwrapRef<CategoryTypeByTree[]>> = ref([])
// 标签
const tagList: Ref<TagType[]> = ref([])

onMounted(async () => {
  await refreshFunc()
  await getCategory()
  await getTag()
})

async function getCategory() {
  const { data } = await articleCategoryByTree()
  birdCateList.value = data
}

const value = ref<string[]>([])
const options = ref<CascaderProps['options']>([])
// 修改选择不显示分类名称问题

function handleCategoryChange(selectedValue: string[]) {
  // 在这里可以获取用户选择的值

  if (selectedValue.length === 0)
    return null // 如果数组为空，返回 null 或者适当的默认值

  const selected = selectedValue[selectedValue.length - 1]
  const cateID = Number.parseInt(selected, 10)

  formData.value.categoryId = cateID

  const categoryName = getCategoryNameById(cateID)

  formData.value.categoryName = categoryName
}

function getCategoryNameById(categoryId: number): string | null {
  // 在 birdCateList 数组中查找对应的对象
  const category = birdCateList.value.find(item => item.id === categoryId)

  // 如果找到了对应的对象，则返回其 categoryName 属性，否则返回 null
  return category ? category.categoryName : null;
}

async function fetchData() {
  const { data } = await articleCategoryByTree()
  console.log('ddddd:', data)

  const convertToTree = (data, parentId) => {
    const result = []
    data.filter(item => item.parentId === parentId).forEach((item) => {
      const newItem = {
        value: item.id.toString(),
        label: item.categoryName,
      }
      const children = convertToTree(data, item.id)
      if (children.length > 0)
        newItem.children = children

      result.push(newItem)
    })
    return result
  }

  const treeData = convertToTree(data, 0)
  console.log(treeData)

  options.value = treeData
}

fetchData()

async function getTag() {
  const { data } = await articleTag()
  tagList.value = data
}

type Key = string | number

const state = reactive<{
  selectedRowKeys: Key[]
  loading: boolean
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
})

interface DataType {
  id: string
  title: string
  key: string
  orderNum: number
  status: boolean
  createTime: string
}

const loading = ref(false)
const tabData: Ref<UnwrapRef<DataType[]>> = ref([])

/**
 * 选中表格
 */
function onSelectChange(selectedRowKeys: Key[]) {
  state.selectedRowKeys = selectedRowKeys
}

const columns: any = [
  {
    title: '编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '封面',
    dataIndex: 'articleCover',
    align: 'center',
  },
  {
    title: '标题',
    dataIndex: 'articleTitle',
    align: 'center',
  },
  {
    title: '类型',
    dataIndex: 'articleType',
    align: 'center',
  },
  {
    title: '分类',
    dataIndex: 'categoryName',
    align: 'center',
  },
  {
    title: '标签',
    dataIndex: 'tagsName',
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'status',
    align: 'center',
  },
  {
    title: '是否顶置',
    dataIndex: 'isTop',
    align: 'center',
  },
  {
    title: '访问量',
    dataIndex: 'visitCount',
    align: 'center',
  },
  {
    title: '作者',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '发布时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
  },
]

async function refreshFunc() {
  loading.value = true
  const { data } = await articleList()
  if (data && data.length > 0) {
    tabData.value = data.map((item: any) => {
      item.isTop = item.isTop === 1
      return item
    })
  }
  loading.value = false
}

async function onFinish(values: any) {
  loading.value = true
  const { data } = await articleSearch(values)
  console.log('查询结果', data)

  if (data && data.length > 0) {
    tabData.value = data.map((item: any) => {
      item.isTop = item.isTop === 1
      return item
    })
  }
  else {
    message.warn('没有查询到相关文章')
    tabData.value = []
  }

  loading.value = false
}

// 修改状态
function updateArticleStatusFunc(id: string, status: number, record: any) {
  record.statusLoading = true
  updateArticleStatus({ id, status }).then((res) => {
    if (res.code === 200) {
      message.success('状态修改成功')
      record.statusLoading = false
    }
  })
}

// 是否顶置
function updateIsTopFunc(id: string, isTop: boolean, record: any) {
  record.isToploading = true
  updateArticleTop({ id, isTop: isTop ? 1 : 0 }).then((res) => {
    if (res.code === 200) {
      message.success('操作成功')
      record.isToploading = false
    }
  })
}

// 删除文章
function onDelete(ids?: string[]) {
  if (ids) {
    deleteArticle(ids).then((res) => {
      if (res.code === 200) {
        message.success('删除成功')
        refreshFunc()
      }
    })
  }
  else {
    // 批量删除
    const ids = state.selectedRowKeys
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的文章吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteArticle(ids as string[]).then((res) => {
          if (res.code === 200) {
            message.success('删除成功')
            refreshFunc()
          }
        })
      },
    })
  }
}
</script>

<template>
  <layout
    :form-state="formData"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item label="标题" name="articleTitle" style="margin-right: 1rem">
        <a-input v-model:value="formData.articleTitle" placeholder="输入文章标题" style="width: 15em" />
      </a-form-item>
      <a-form-item label="分类" name="categoryId" style="margin-right: 1rem">
        <a-space>
          <a-cascader
            v-model:value="formData.categoryName"
            :options="options"
            expand-trigger="hover"
            placeholder="选择鸟分类"
            style="width: 15em"
            @change="handleCategoryChange"
          />
        </a-space>
      </a-form-item>
      <a-form-item label="状态" name="status" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-model:value="formData.status"
            style="width: 10em"
            placeholder="选择状态"
          >
            <a-select-option :value="1">
              公开
            </a-select-option>
            <a-select-option :value="2">
              私密
            </a-select-option>
            <a-select-option :value="3">
              草稿
            </a-select-option>
          </a-select>
        </a-space>
      </a-form-item>
      <a-form-item label="是否顶置" name="isTop" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.isTop"
          style="width: 13em"
          placeholder="是否顶置"
        >
          <a-select-option :value="1">
            是
          </a-select-option>
          <a-select-option :value="0">
            否
          </a-select-option>
        </a-select>
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" style="margin-right: 10px" @click="$router.push({ path: '/bird/essay/publish' })">
        <template #icon>
          <PlusOutlined />
        </template>
        添加
      </a-button>
      <a-button type="dashed" danger ghost :disabled="state.selectedRowKeys.length === 0" @click="onDelete()">
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <a-button
        class="green"
        style="margin-right: 10px"
        :disabled="state.selectedRowKeys.length === 0 || state.selectedRowKeys.length > 1"
        @click="$router.push({ path: '/bird/essay/publish', query: { id: state.selectedRowKeys[0] } })"
      >
        <template #icon>
          <FileSyncOutlined />
        </template>
        修改
      </a-button>
    </template>
    <template #table-content>
      <a-table
        :columns="columns"
        :data-source="tabData"
        :loading="loading"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :row-key="record => record.id"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'articleCover'">
            <a-image :src="record.articleCover" style="width: 70px; height: 50px" />
          </template>
          <template v-if="column.dataIndex === 'categoryName'">
            <a-tag>{{ record.categoryName }}</a-tag>
          </template>
          <template v-if="column.dataIndex === 'tagsName'">
            <template v-if="record.tagsName.length <= 2">
              <a-tag v-for="tag in record.tagsName" :key="tag" color="blue">
                {{ tag }}
              </a-tag>
            </template>
            <template v-else>
              <a-popover trigger="hover">
                <template #content>
                  <a-tag v-for="tag in record.tagsName" :key="tag" color="blue">
                    {{ tag }}
                  </a-tag>
                </template>
                <a-tag v-for="tag in record.tagsName.slice(0, 1)" :key="tag" color="blue">
                  {{ tag }}
                </a-tag>
                <a-tag>
                  +{{ record.tagsName.length - 1 }}
                </a-tag>
              </a-popover>
            </template>
          </template>
          <template v-if="column.dataIndex === 'status'">
            <a-space>
              <a-select
                v-model:value="record.status"
                style="width: 6em"
                size="small"
                :loading="record.statusLoading"
                @change="updateArticleStatusFunc(record.id, record.status, record)"
              >
                <a-select-option :value="1">
                  公开
                </a-select-option>
                <a-select-option :value="2">
                  私密
                </a-select-option>
                <a-select-option :value="3">
                  草稿
                </a-select-option>
              </a-select>
            </a-space>
          </template>
          <template v-if="column.dataIndex === 'isTop'">
            <a-switch
              v-model:checked="record.isTop"
              checked-children="是"
              un-checked-children="否"
              :loading="record.isToploading"
              @change="updateIsTopFunc(record.id, record.isTop, record)"
            />
          </template>
          <template v-if="column.dataIndex === 'visitCount'">
            <span style="color: darkred"><FireFilled /></span>
            {{ record.visitCount }}
          </template>
          <template v-if="column.dataIndex === 'operation'">
            <a :href="`http://localhost:6060/article/${record.id}`" target="_blank">
              <a-button type="link" style="padding: 0">
                <template #icon>
                  <LinkOutlined />
                </template>
                <span style="margin-left: 3px">跳转</span>
              </a-button>
            </a>
            <a-button type="link" style="padding: 0" @click="$router.push({ path: '/bird/essay/publish', query: { id: record.id } })">
              <template #icon>
                <FileSyncOutlined />
              </template>
              <span style="margin-left: 3px">修改</span>
            </a-button>
            <a-popconfirm
              title="是否确定删除"
              ok-text="Yes"
              cancel-text="No"
              @confirm="onDelete([record.id])"
            >
              <a-button type="link" style="padding: 0">
                <template #icon>
                  <DeleteOutlined />
                </template>
                <span style="margin-left: 3px">删除</span>
              </a-button>
            </a-popconfirm>
          </template>
          <template v-if="column.dataIndex === 'createTime'">
            <a-tooltip placement="top">
              <template #title>
                <span>{{ record.createTime }}</span>
              </template>
              {{ record.createTime.split(' ')[0] }}...
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'articleType'">
            <!--            类型 (1现存，2，濒危，3灭绝) -->
            <a-tag color="#108ee9">
              {{ record.articleType === 1 ? '现存' : record.articleType === 2 ? '濒危' : '灭绝' }}
            </a-tag>
          </template>
        </template>
      </a-table>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
