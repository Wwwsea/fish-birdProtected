<script setup lang="ts">
import 'md-editor-v3/lib/style.css'
import { MdEditor } from 'md-editor-v3'
import type { CascaderProps, UploadProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import type { Ref, UnwrapRef } from 'vue'
import type { BasicColorMode, UseColorModeReturn } from '@vueuse/core'
import { ref } from 'vue'
import {
  addTag,
  articleCategoryByTree,
  articleTag,
  deleteCover,
  getArticle,
  publishArticle,
  uploadArticleImage,
  uploadCover,
} from '~/api/bird/article'
import type { CategoryTypeByTree, TagType } from '~/pages/bird/essay/publish/type.ts'
import { useMultiTab } from '~/stores/multi-tab.ts'

const value = ref<string[]>([])
const options = ref<CascaderProps['options']>([])
const formData = ref({
  categoryId: undefined,
  tagId: undefined,
  articleCover: undefined,
  articleTitle: undefined,
  articleContent: undefined,
  articleType: 1,
  isTop: 0,
  status: 1,
})

function handleCategoryChange(selectedValue: string[]) {
  // 在这里可以获取用户选择的值
  console.log('Selected value:', selectedValue[0])

  formData.value.categoryId = selectedValue[0]
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

const route = useRoute()
const multiTab = useMultiTab()
// 日夜切换
const mode: UseColorModeReturn<BasicColorMode> = useColorMode()

const fileList = ref<UploadProps['fileList']>([])
// 预览Base64
const previewBase64 = ref<string>()

function getBase64(img: Blob, callback: (base64Url: string) => void) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result as string))
  reader.readAsDataURL(img)
}

const VNodes = defineComponent({
  props: {
    vnodes: {
      type: Object,
      required: true,
    },
  },
  render() {
    return this.vnodes
  },
})

const inputRef = ref()
const categoryName = ref()
const tagName = ref()

// 分类
const birdCateList: Ref<UnwrapRef<CategoryTypeByTree[]>> = ref([])
// 标签
const tagList: Ref<TagType[]> = ref([])

onMounted(async () => {
  fetchData()
  await getFormData()
  await getCategory()
  await getTag()
})

async function getCategory() {
  const { data } = await articleCategoryByTree()
  birdCateList.value = data
}

async function getTag() {
  const { data } = await articleTag()
  tagList.value = data
}

const tagLoading = ref(false)

function addTagFunc(e: MouseEvent) {
  tagLoading.value = true
  e.preventDefault()
  const data = { tagName: tagName.value, id: tagList.value[tagList.value.length - 1].id + 1 }
  addTag(data).then((res) => {
    if (res.code === 200)
      tagLoading.value = false
    tagList.value.push(data)
  })
  tagName.value = ''
  setTimeout(() => {
    inputRef.value?.focus()
  }, 0)
}

function beforeUpload(file: UploadProps['fileList'][number]) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('文件格式必须是jpg或png')
    return false
  }

  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    message.error('图片必须小于 5MB')
    return false
  }

  fileList.value = [file]
  getBase64(file, (base64Url: string) => {
    previewBase64.value = base64Url
  })

  return false
}

function onFinish() {
  if (!formData.value.articleTitle || !formData.value.categoryId || !formData.value.tagId || !formData.value.articleContent) {
    message.warn('请检查是否填写完整')
    return
  }

  if (!formData.value.articleCover && !fileList.value[0]) {
    message.warn('请上传文章封面')
    return
  }

  if (!fileList.value[0] && formData.value.articleCover) {
    publishArticle(formData.value).then((res) => {
      if (res.code === 200)
        message.success('发布成功')

      else
        message.error(`发布失败`)
    })
  }
  else {
    const articleCover = new FormData()
    articleCover.append('articleCover', fileList.value[0])
    uploadCover(articleCover).then((res) => {
      if (res.code === 200) {
        const articleCover = res.data
        formData.value.articleCover = res.data
        publishArticle(formData.value).then((res) => {
          if (res.code === 200) {
            message.success('发布成功')
            formData.value.categoryId = undefined
            formData.value.tagId = undefined
            formData.value.articleCover = undefined
            formData.value.articleTitle = undefined
            formData.value.articleContent = undefined
            formData.value.articleType = 1
            formData.value.isTop = 0
            formData.value.status = 1
            fileList.value = []
            previewBase64.value = ''
          }
          else {
            message.error(`发布失败`)
            deleteCover(articleCover)
          }
        }).catch(() => {
          deleteCover(articleCover)
        })
      }
      else {
        message.error(`上传文章封面失败`)
      }
    })
  }
}

async function onUploadArticleImg(files: any, callback: any) {
  const res = await Promise.all(
    files.map((file) => {
      return new Promise((rev, rej) => {
        const form = new FormData()
        form.append('articleImage', file)
        uploadArticleImage(form).then((res) => {
          if (res.code === 200)
            rev(res.data)
        }).catch(error => rej(error))
      })
    }),
  )
  callback(res)
}

const toolbars = [
  'bold',
  'underline',
  'italic',
  '-',
  'title',
  'strikeThrough',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  'task',
  '-',
  'codeRow',
  'code',
  'link',
  'image',
  'table',
  'mermaid',
  'katex',
  '-',
  'revoke',
  'next',
  'save',
  '=',
  'pageFullscreen',
  'fullscreen',
  'preview',
  'htmlPreview',
  'catalog',
]

// 数据回显
function getFormData() {
  if (route.query.id) {
    getArticle(route.query.id as string).then((res) => {
      console.log(res.data)
      if (res.data) {
        formData.value = res.data
        console.log('回显数据显示：：：：：', formData.value.categoryId)
      }
    })
  }
}

function close() {
  multiTab.close(route.fullPath)
}
</script>

<template>
  <layout :is-list="false">
    <template #form-items>
      <a-form-item label="标题" style="margin-right: 1rem">
        <a-input v-model:value="formData.articleTitle" placeholder="输入文章标题" style="width: 15em" />
      </a-form-item>

      <a-form-item label="分类" style="margin-right: 1rem">
        <!--        <a-space> -->
        <!--          <a-select -->
        <!--            v-if="categoryList" -->
        <!--            v-model:value="formData.categoryId" -->
        <!--            :loading="categoryLoading" -->
        <!--            placeholder="选择分类" -->
        <!--            style="width: 15em" -->
        <!--            :options="categoryList.map(item => ({ value: item.id, label: item.categoryName }))" -->
        <!--          > -->
        <!--            <template #dropdownRender="{ menuNode: menu }"> -->
        <!--              <VNodes :vnodes="menu" /> -->
        <!--              <a-divider style="margin: 4px 0" /> -->
        <!--              <a-space style="padding: 4px 8px"> -->
        <!--                <a-input ref="inputRef" v-model:value="categoryName" placeholder="添加分类" /> -->
        <!--                <a-button type="text" @click="addCategoryFunc"> -->
        <!--                  <template #icon> -->
        <!--                    <plus-outlined /> -->
        <!--                  </template> -->
        <!--                  添加 -->
        <!--                </a-button> -->
        <!--              </a-space> -->
        <!--            </template> -->
        <!--          </a-select> -->
        <!--        </a-space> -->
        <a-space>
          <a-cascader
            v-model:value="formData.categoryId"
            :options="options"
            expand-trigger="hover"
            placeholder="选择鸟分类"
            style="width: 15em"
            @change="handleCategoryChange"
          /> <!--             -->
        </a-space>
      </a-form-item>

      <a-form-item label="标签" style="margin-right: 1rem">
        <a-select
          v-if="tagList"
          v-model:value="formData.tagId"
          mode="multiple"
          :loading="tagLoading"
          placeholder="选择标签"
          style="width: 15em"
          :options="tagList.map(item => ({ value: item.id, label: item.tagName }))"
        >
          <template #dropdownRender="{ menuNode: menu }">
            <VNodes :vnodes="menu" />
            <a-divider style="margin: 4px 0" />
            <a-space style="padding: 4px 8px">
              <a-input ref="inputRef" v-model:value="tagName" placeholder="添加标签" />
              <a-button type="text" @click="addTagFunc">
                <template #icon>
                  <plus-outlined />
                </template>
                添加
              </a-button>
            </a-space>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item label="类型" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.articleType"
          style="width: 15em"
        >
          <a-select-option :value="1">
            现存
          </a-select-option>
          <a-select-option :value="2">
            濒危
          </a-select-option>
          <a-select-option :value="3">
            灭绝
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="状态" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-model:value="formData.status"
            style="width: 120px"
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
      <a-form-item label="是否顶置" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.isTop"
          style="width: 13em"
        >
          <a-select-option :value="1">
            是
          </a-select-option>
          <a-select-option :value="0">
            否
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-space>
          <template v-if="previewBase64 || formData.articleCover">
            <a-popover title="预览">
              <template #content>
                <a-image
                  :width="200"
                  :src="previewBase64 || formData.articleCover"
                />
              </template>
              <a-upload
                :file-list="fileList"
                :before-upload="beforeUpload"
                :max-count="1"
                :show-upload-list="false"
              >
                <a-button>
                  <PictureOutlined />
                  上传封面
                </a-button>
              </a-upload>
            </a-popover>
          </template>
          <template v-else>
            <a-upload
              :file-list="fileList"
              :before-upload="beforeUpload"
              :max-count="1"
              :show-upload-list="{ showRemoveIcon: false }"
            >
              <a-button>
                <PictureOutlined />
                上传封面
              </a-button>
            </a-upload>
          </template>
          <a-button type="primary" @click="onFinish">
            发布
          </a-button>
          <a-button class="orange" style="margin-right: 10px" @click="close">
            <template #icon>
              <CloseOutlined />
            </template>
            关闭
          </a-button>
        </a-space>
      </a-form-item>
    </template>
    <template #table-content>
      <div style="height: 80vh;width: 100%">
        <MdEditor
          v-model="formData.articleContent" :theme="mode" style="height: 80vh" :toolbars="toolbars as []"
          @onUploadImg="onUploadArticleImg"
        />
      </div>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
