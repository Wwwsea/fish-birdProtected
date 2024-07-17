<script setup lang="ts">
import 'md-editor-v3/lib/style.css'
import { MdEditor } from 'md-editor-v3'
import type { CascaderProps, UploadProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import type { Ref, UnwrapRef } from 'vue'
import type { BasicColorMode, UseColorModeReturn } from '@vueuse/core'
import { ref } from 'vue'
import dayjs from 'dayjs'
import {
  addTag,
  articleCategoryByTree,
  articleTag,
  deleteCover,
  deleteThreeD,
  getArticle,
  publishArticle,
  uploadArticleImage,
  uploadCover,
  uploadThreeDImage,
} from '~/api/bird/article'
import type { CategoryTypeByTree, TagType } from '~/pages/bird/essay/publish/type.ts'
import { useMultiTab } from '~/stores/multi-tab.ts'

import 'dayjs/locale/zh-cn'

// 引入中文语言包

// 设置 Day.js 使用中文语言包
dayjs.locale('zh-cn')

const value = ref<string[]>([])
const options = ref<CascaderProps['options']>([])
const formData = ref({
  categoryId: undefined,
  categoryName: undefined,
  tagId: undefined,
  articleCover: undefined,
  threeDImage: undefined,
  articleTitle: undefined,
  articleContent: undefined,
  articleType: 1,
  isTop: 0,
  status: 1,
  extinctionDate: undefined,
})

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
  return category ? category.categoryName : null
}

async function fetchData() {
  const { data } = await articleCategoryByTree()

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

  options.value = treeData
}

fetchData()

const route = useRoute()
const multiTab = useMultiTab()
// 日夜切换
const mode: UseColorModeReturn<BasicColorMode> = useColorMode()

const fileList = ref<UploadProps['fileList']>([])

const threeFileList = ref<UploadProps['threeFileList']>([])

// 预览Base64
const previewBase64 = ref<string>()

function getBase64(: Blob, callback: (base64Url: string) => void) {
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
    inputRef.value?.focus() // 焦点重新定位到输入框
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
  console.log('图片上传=========', fileList.value)
  getBase64(file, (base64Url: string) => {
    previewBase64.value = base64Url
  })

  return false
}

function beforeUploadThreeD(file: UploadProps['threeFileList'][number]) {
  const isZip = file.type === 'application/zip' || file.name.endsWith('.zip')
  if (!isZip) {
    message.error('文件格式必须是 zip')
    return false
  }

  const isLt5M = file.size / 1024 / 1024 < 10
  if (!isLt5M) {
    message.error('文件必须小于 10MB')
    return false
  }

  threeFileList.value = [file]
  console.log('3D模型上传=========', threeFileList.value)
  getBase64(file, (base64Url: string) => {
    previewBase64.value = base64Url
  })

  return false
}

// 监听键盘事件
window.addEventListener('keydown', (event) => {
  // 检查是否按下了 Alt 键和 'o' 键（按下 fn 键无法在浏览器中识别）
  if (event.altKey && event.key === 'o') {
    event.preventDefault() // 防止浏览器默认行为
    onFinish() // 执行发布逻辑
  }
})

function onFinish() {
  if (!formData.value.articleTitle || !formData.value.categoryId || !formData.value.tagId || !formData.value.articleContent) {
    message.warn('请检查是否填写完整')
    return
  }

  if (!formData.value.articleCover && !fileList.value[0]) {
    message.warn('请上传文章封面')
    return
  }
  formData.value.extinctionDate = formData.value.extinctionDate && dayjs(formData.value.extinctionDate).isValid() ? dayjs(formData.value.extinctionDate).format('YYYY-MM-DD HH:mm:ss') : undefined

  if (!fileList.value[0] && formData.value.articleCover) {
    console.log('22222222222222', formData.value, ' ttttt:', formData.value.extinctionDate)
    publishArticle(formData.value).then((res) => {
      if (res.code === 200)
        message.success('发布成功', formData.value.articleCover, formData.value.threeDImage)
      else
        message.error(`发布失败`)
    })
  }
  else {
    const articleCover = new FormData()
    articleCover.append('articleCover', fileList.value[0])
    const threeDImage = new FormData()
    threeDImage.append('threeDImage', threeFileList.value[0])
    const uploadCoverPromise = uploadCover(articleCover)
    const uploadThreeDPromise = uploadThreeDImage(threeDImage)

    Promise.all([uploadCoverPromise, uploadThreeDPromise])
      .then(([coverRes, threeDRes]) => {
        if (coverRes.code === 200) {
          formData.value.articleCover = coverRes.data
        }
        else {
          message.error('上传文章封面失败')
          throw new Error('上传文章封面失败')
        }

        if (threeDRes && threeDRes.code === 200)
          formData.value.threeDImage = threeDRes.data
        // formData.value.extinctionDate = formData.value.extinctionDate ? dayjs(formData.value.extinctionDate).format('YYYY-MM-DD HH:mm:ss') : undefined
        console.log('0000008888000000', formData.value, ' ttttt:', formData.value.extinctionDate)
        return publishArticle(formData.value)
      })
      .then((res) => {
        if (res.code === 200) {
          message.success('发布成功')
          resetFormData()
        }
        else {
          message.error('3D模型异常发布失败')
          deleteCoverAndThreeD()
        }
      })
      .catch(() => {
        message.error('因为图片异常发布失败')
        deleteCoverAndThreeD()
      })

    function deleteCoverAndThreeD() {
      if (formData.value.articleCover)
        deleteCover(formData.value.articleCover)

      if (formData.value.threeDImage)
        deleteThreeD(formData.value.threeDImage)
    }

    function resetFormData() {
      formData.value.categoryId = undefined
      formData.value.tagId = undefined
      formData.value.articleCover = undefined
      formData.value.threeDImage = undefined
      formData.value.articleTitle = undefined
      formData.value.articleContent = undefined
      formData.value.articleType = 1
      formData.value.isTop = 0
      formData.value.status = 1
      fileList.value = []
      threeFileList.value = []
      previewBase64.value = ''
    }
  }
}

// 修改上传文章图片重复问题
async function onUploadArticleImg(files: any, callback: any) {
  let uploadedCount = 0 // 计数器初始化为0
  const res = await Promise.all(
    files.map((file) => {
      return new Promise((rev, rej) => {
        const form = new FormData()
        form.append('articleImage', file)
        uploadArticleImage(form).then((res) => {
          if (res.code === 200)
            rev(res.data)
        }).catch(error => rej(error)).finally(() => {
          uploadedCount++ // 每次处理完一个文件，计数器加一
          console.log('已处理图片。。。。', uploadedCount)

          if (uploadedCount === files.length) { // 当所有文件都处理完成后才调用回调函数
            callback(res)
          }
        })
      })
    }),
  )
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
      <!-- 灭绝时间选择 -->
      <a-form-item v-if="formData.articleType === 3" label="灭绝时间">
        <a-date-picker v-model:value="formData.extinctionDate" style="width: 15em" />
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

          <a-upload
            :file-list="threeFileList"
            :before-upload="beforeUploadThreeD"
            :max-count="1"
            :show-upload-list="{ showRemoveIcon: false }"
          >
            <a-button>
              <template #icon>
                <PlaySquareOutlined />
              </template>
              3D模型
              <!--              {{ threeFileList.length > 0 ? threeFileList[0].name : '3D模型' }} -->
            </a-button>
          </a-upload>

          <a-button type="primary" accesskey="o" @click="onFinish">
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
      <div style="height: 180vh;width: 100%">
        <MdEditor
          v-model="formData.articleContent" :theme="mode" style="height: 180vh" :toolbars="toolbars as []"
          @onUploadImg="onUploadArticleImg"
        />
      </div>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
