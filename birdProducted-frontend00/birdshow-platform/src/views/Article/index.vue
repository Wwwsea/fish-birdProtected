<script setup lang="ts">
import {ref} from 'vue'
import {MdCatalog, MdPreview} from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import {
  addArticleVisit,
  getArticleDetail
} from "@/apis/article";
import {cancelFavorite, userFavorite, isFavorite} from '@/apis/favorite'
import {cancelLike, isLike, userLike} from '@/apis/like';
import DirectoryCard from "./DirectoryCard/index.vue";
import {ElMessage} from "element-plus";
import {Close} from "@element-plus/icons-vue";
import router from "@/router";
import {useColorMode} from "@vueuse/core";

const mode = useColorMode()
const id = 'preview-only';
const scrollElement = document.documentElement;

const articleDetail = ref({
  articleCover: '',
  articleTitle: '',
  articleContent: undefined,
  categoryName: '',
  categoryId: '',
  tags: [],
  visitCount: 0,
  commentCount: 0,
  likeCount: 0,
  favoriteCount: 0,
  createTime: '',
  updateTime: '',
  userId: 0,
  id: "0"
})

const route = useRoute();

// 是否加载
const loading = ref(false)

// 字数 统计
const countMd = ref(0)

// 监听路由变化
watch(() => route.params.id, () => {
  getArticleDetailById()
})


onMounted(async () => {
  await getArticleDetailById()

  if ($refs.headTitle) {
    $refs.headTitle.addEventListener('mousemove', handleMouseMove);
  } else {
    console.error('Cannot find ref: headTitle');
  }
}
)


async function getArticleDetailById() {
  getArticleDetail(route.params.id).then(res => {
    if(!res.data){
      ElMessage.warning({
        message: '文章不存在',
      })
      // 跳转回去
      router.push({path: '/'})
      return
    }
    if (route.params.id){
       addArticleVisit(route.params.id)
    }
    // 时间去掉时分秒
    res.data.createTime = res.data.createTime.split(' ')[0]
    res.data.updateTime = res.data.updateTime.split(' ')[0]
    articleDetail.value = res.data
    loading.value = true
    // 收藏
    isFavoriteFunc()
    // 点赞
    isLikeFunc()
  })
}

function mdHtml(htmlText: string) {
  // 获取html中的所有文字，去掉空格与标点符号
  const text = htmlText.replace(/<[^>]+>/g, "").replace(/[\r\n]/g, "").replace(/[ ]/g, "").replace(/[\s+\.\!\/_,$%^*(+\"\']+|[+——！，。？、~@#￥%……&*（）]+/g, "")
  countMd.value = <number>countWords(text.length)
}

// 字数统计
function countWords(count: number) {
  if (count <= 1000) {
    return count
  } else {
    let counts = (count / 1000);
    // 留小数点一位数
    counts = Number(counts.toFixed(1));
    return counts + 'k';
  }
}


// 分享
const copyToClipboard = async () => {
  try {
    const content = `欢迎访问鸟类文章：${articleDetail.value.articleTitle} \n通往地址：VITE_SERVE='http://127.0.0.1:6060/${route.path}`;
    // 替换为你要分享的实际内容
    await navigator.clipboard.writeText(content);
    ElMessage.success("已复制分享链接");
  } catch (error) {
    ElMessage.error("复制失败，请类型网站管理员");
  }
}

// 收藏标记
const collection = ref(false)
// 点赞标记
const like = ref(false)

const collectionBtn = (detail: object) => {

  if (collection.value) {
    // 取消收藏
    cancelFavorite(1, articleDetail.value.id).then(res => {
      if (res.code === 200) {
        detail.favoriteCount -= 1
        collection.value = false
        ElMessage.info("取消收藏");
      } else {
        ElMessage.error(res.msg);
      }
    })
  } else {
    // 收藏
    userFavorite(1, articleDetail.value.id).then(res => {
      if (res.code === 200) {
        detail.favoriteCount += 1
        collection.value = true
        ElMessage.success("收藏成功");
      } else {
        ElMessage.error(res.msg);
      }
    })
  }
}

function likeBtn(detail: object) {
  if (like.value) {
    cancelLike(1, articleDetail.value.id).then(res => {
      if (res.code === 200) {
        detail.likeCount -= 1
        like.value = false
        ElMessage.info("我会继续努力的");
      } else {
        ElMessage.error(res.msg);
      }
    })
  } else {
    userLike(1, articleDetail.value.id).then(res => {
      if (res.code === 200) {
        detail.likeCount += 1
        like.value = true
        ElMessage.success("感谢你的认可");
      } else {
        ElMessage.error(res.msg);
      }
    })
  }
}

// 是否收藏
function isFavoriteFunc() {
  isFavorite(1, articleDetail.value.id).then(res => {
    collection.value = res.data === true;
  })
}

// 是否点赞
function isLikeFunc() {
  isLike(1, articleDetail.value.id).then(res => {
    like.value = res.code === 200;
  })
}

const isShowMoveCatalog = ref(false)

const mouseX = ref(0);
const mouseY = ref(0);
const backgroundImageOffsetX = ref(0);
const backgroundImageOffsetY = ref(0);

const handleMouseMove = (event: MouseEvent) => {
  mouseX.value = event.clientX;
  mouseY.value = event.clientY;

  backgroundImageOffsetX.value = (mouseX.value / window.innerWidth - 0.5) * 100;
  backgroundImageOffsetY.value = (mouseY.value / window.innerHeight - 0.5) * 100;
};


</script>

<template>
  <Main is-side-bar>
    <template #header>
      <Header/>
    </template>
    <template #content>
      <div class="head_title"
           :style="{
                    backgroundImage: `url(${articleDetail.articleCover})`,
                    '--bg-offset-x': `${backgroundImageOffsetX}%`,
                    '--bg-offset-y': `${backgroundImageOffsetY}%`
                    }"
           @mousemove="handleMouseMove"
           ref="headTitle"
      >
        <div class="head_title_text">
          <div class="classify">
            <div>{{ articleDetail.categoryName }}</div>
            <div class="tag" v-for="tag in articleDetail.tags"># {{ tag.tagName }}</div>
          </div>
          <div class="title">{{ articleDetail.articleTitle }}</div>
          <div class="statistics">
            <div>字数统计:{{ countMd }}</div>
          </div>
          <div class="statistics">
<!--            <div>访问量:{{ articleDetail.visitCount }}</div>-->
            <div>评论数:{{ articleDetail.commentCount }}</div>
            <div>点赞量:{{ articleDetail.likeCount }}</div>
<!--            <div>收藏量:{{ articleDetail.favoriteCount }}</div>-->
          </div>
          <div class="time">
            <div>发布：{{ articleDetail.createTime }}</div>
            <div>更新：{{ articleDetail.updateTime }}</div>
          </div>
        </div>
      </div>
      <div>
        <template v-if="articleDetail.articleContent">
          <!-- 富文本预览 -->
          <MdPreview :editorId="id" :theme="mode" :modelValue="articleDetail.articleContent" :on-html-changed="mdHtml"/>
        </template>
        <el-divider border-style="dashed" content-position="center">
          <div style="display: flex; align-items: center; justify-content: center;">
            <span >感谢观看 </span>

          </div>
        </el-divider>
        <div class="threeModel">
          <div class="tipping">
            <div>
              <svg-icon name="threejs"/>
              <span @click="$router.push({name:'ThreeDModel',params:{id:route.params.id}})">点查看3D模型</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 尾部标签与点赞收藏分享 -->
      <div style="display: flex; justify-content: space-between; ">

        <div class="tag">
          <template v-for="tag in articleDetail.tags" :key="tag.id"><span style="font-size: 1.4em;">标签：</span>
            <div @click="$router.push(`/tags/${tag.id}`)" style="margin-right: 2em;"># {{ tag.tagName }}</div>
          </template>
        </div>
        <div class="like" style="display: flex;">
          <div @click="likeBtn(articleDetail)">
            <SvgIcon v-show="!like" name="like"/>
            <SvgIcon v-show="like" name="like-selected"/>
            <span>{{ articleDetail.likeCount }}</span>
          </div>
          <div @click="collectionBtn(articleDetail)">
            <SvgIcon v-show="!collection" name="collection"/>
            <SvgIcon v-show="collection" name="collection-selected"/>
            <span>{{ articleDetail.favoriteCount }}</span>
          </div>
          <div @click="copyToClipboard">
            <SvgIcon name="share"/>
            <span>分享</span>
          </div>
        </div>
      </div>
      <div>
        <div class="tag" style="display: flex;justify-content: left;"><span style="font-size: 1.4em;">分类：</span>
          <div @click="$router.push(`/category/${articleDetail.categoryId}`)">{{ articleDetail.categoryName }}</div>
        </div>
      </div>
      <!-- 上/下 篇文章-->
      <div class="goOn">
        <!-- 上一篇 -->
        <div>
          <div v-if="articleDetail.preArticleId > 0">
            <el-link @click="$router.push(`/article/${articleDetail.preArticleId}`)">
              上一篇：{{ articleDetail.preArticleTitle }}
            </el-link>
          </div>
        </div>
        <!-- 下一篇 -->
        <div>
          <div v-if="articleDetail.nextArticleId > 0">
            <el-link @click="$router.push(`/article/${articleDetail.nextArticleId}`)">
              下一篇：{{ articleDetail.nextArticleTitle }}
            </el-link>
          </div>
        </div>
      </div>
      <!-- 用户评论 -->
      <Comment :type="1" :like-type="2" :author-id="articleDetail.userId" :type-id="articleDetail.id" v-if="loading"/>
    </template>
    <template #information>
      <!-- 侧边栏 -->
      
      <CardInfo/>
      <div class="threeModel" style="padding-top: 45px">
        <el-affix :offset="12">

          <div class="tipping">
            <div style=" width: 200px;height: 300px;">
              <svg-icon name="threejs"/>
              <span @click="$router.push({name:'ThreeDModel',params:{id:route.params.id}})">点查看3D模型</span>
            </div>
          </div>
          <div class="directory" style="margin-top: 2em">
            <DirectoryCard/>
          </div>

        </el-affix>
        </div>

    </template>
    <template #footer>
      <Footer/>
    </template>
  </Main>
  <ToTop/>
  <div>
    <el-affix position="bottom" :offset="200">
      <el-tooltip
          effect="light"
          content="显示目录"
          placement="right"
      >
      <div class="move_catalog_btn" @click="isShowMoveCatalog = true" >
        <svg-icon name="directory" class="move_catalog_svg" width="30" height="30"/>
      </div>
      </el-tooltip>
    </el-affix>
  </div>
  <div>
    <el-drawer v-model="isShowMoveCatalog" :with-header="true" size="60%" direction="rtl" :show-close="false">
      <template #header>
        <span style="font-size: 1.2rem">目录</span>
        <el-button :icon="Close" style="background: none;font-size: 1.5rem;width: 30px;border: none"
                   @click="isShowMoveCatalog = false"/>
      </template>
      <template #default>
        <div class="move_catalog">
          <MdCatalog :editorId="id" :scrollElement="scrollElement"/>
        </div>
      </template>
    </el-drawer>
  </div>

</template>

<style scoped lang="scss">
@import "@/styles/mixin.scss";

// 移动端目录按钮
.move_catalog_btn {
  border-radius: 50%;
  box-shadow: var(--el-box-shadow-light);
  border: 1px solid var(--el-border-color);
  background: white;
  // 固定在右下角
  position: fixed;
  right: 2em;
  bottom: 8em;
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  visibility: hidden;
  @media screen and (max-width: 910px) {
    visibility: visible;
  }

  .move_catalog_svg{
    @media screen and (max-width: 768px) {
        width: 25px !important;
        height: 25px !important;
    }
  }
}
// 移动端目录
.move_catalog{
  :deep(.md-editor-catalog-link){
    span{
      font-size: 1.05em;
    }
    padding: 1rem;
  }
}

:deep(.el-drawer__header){
  margin-bottom: 0;
}

// 目录
:deep(.md-editor-catalog-link .md-editor-catalog-link:hover) {
  background: #eeeeee;
  border-left: 2px solid #eeeeee;
  transition: all 0.3s;
}
:deep(.md-editor-catalog-wrapper .md-editor-catalog-link){
  border-left: 2px solid saddlebrown;
  transition: all 0.3s;
}

:deep(.md-editor-catalog-link){
  @media screen and (max-width: 768px) {
    font-size: 0.5em;
  }
}

.head_title {
  border-radius: $border-radius;
  height: 40vh;
  width: 100%;

  // 调整大小以覆盖整个背景区域
  background-size: cover;
  //background-position: center;
  background-position: calc(50% + var(--bg-offset-x)) calc(50% + var(--bg-offset-y));
  background-repeat: no-repeat;

  .head_title_text {
    display: flex;
    flex-direction: column;
    align-items: self-start;
    color: white;
    font-size: 15px;
    padding: 5%;

    .tag {
      // 背景透明度0
      background-color: rgba(255, 255, 255, 0);
    }

    div div {
      background-color: rgba(255, 255, 255, 0.3);
      border-radius: 5px;
      margin: 5px;
      padding: 5px;
    }

    div {
      display: flex;
    }

    .title {
      font-size: 40px;
      margin: 10px 0;
    }
  }
}

.copyright {
  font-size: 0.8em;
  margin: 1rem 0;
  padding: 1rem 2rem;
  border-radius: 0.625rem;
  //background-color: var(--el-background-color);
  border: 1px solid var(--el-border-color);

  div {
    display: flex;
    align-items: center;
    margin: 1rem 0;
  }

  div strong {
    margin: 0 0.5rem;
    font-weight: bold;
  }
}

// 文章底部标签
.tag {
  font-size: 0.8em;
  align-items: center;
  display: flex;
  flex-wrap: wrap;

  div {
    margin-right: 2em;
    //margin: 0.5rem 0.5rem;
    padding: 0.5rem 0.9rem;
    border: 1px solid var(--el-border-color);
    border-radius: 5px;
    background-color: var(--el-background-color);

    &:hover {
      background-color: var(--el-border-color);
      cursor: pointer;
    }
  }
}

.like {
  font-size: 0.8em;
  display: flex;
  flex-wrap: wrap;

  div {
    @include flex;
    margin: 0 0.5rem;
    padding: 0.5rem 0.9rem;
    border-radius: 5px;
    background-color: var(--el-background-color);

    span {
      margin-left: 0.5em;
    }

    &:hover {
      background-color: var(--el-border-color);
      cursor: pointer;
    }
  }
}

.tipping {
  @include flex;
  width: 100%;
  text-align: center;
  font-size: 0.86em;
  font-weight: bold;
  cursor: pointer;

  div {
    @include flex;
    color: white;
    background-color: var(--bird-bg-threeD);
    width: 20%;
    border: 1px solid var(--el-border-color);
    height: 2.5em;
    border-radius: 5px;

    span {
      margin-left: 0.6em;
    }

    &:hover {
      background-color: #a7a7a7;
    }
  }
}

// 打赏二维码
.qrCode {
  @include flex;
  align-items: center;
  width: 100%;
  height: 100%;

  div {
    @include flex;
    flex-direction: column-reverse;
    margin: 0 0.5rem;
  }

  .el-image {
    width: 9em;
    height: 9em;
  }
}

.goOn {
  @include flex;
  justify-content: space-between;
  margin: 1rem 0;

  div {
    @include flex;
    align-items: center;
    color: var(--el-text-color-secondary);
    cursor: pointer;

    div {
      .el-link {
        font-size: 0.6em;
      }
    }
  }
}

:deep(.md-editor-preview-wrapper){
  @media screen and (max-width: 910px) {
    padding: 0.2rem;
  }
}

:deep(.md-editor){
  // 主题切换配置
}
</style>