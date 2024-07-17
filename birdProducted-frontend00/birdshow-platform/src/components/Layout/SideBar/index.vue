<!-- 侧边栏 -->
<template>
  <div id ="xxxxxxx"></div>

  <div>
    <div>
      <Card title="公告" prefixIcon="announcement" suffix-icon="jt_y" :isDithering="true" :isArrow="true">
        <span @click="$router.push(`/message`)">{{ useWebsite.webInfo?.sidebarAnnouncement }}</span>
      </Card>
    </div>

    <!-- <div>
      <RandomArticle/>
    </div> -->

  </div>

</template> 

<script setup lang="ts">
import Card from '@/components/Card/index.vue'
import useWebsiteStore from "@/store/modules/website.ts";

const useWebsite = useWebsiteStore()


</script>

<style lang="scss" scoped>

  .statistics {
  display: flex;
  flex-direction: column;
  color: $menuActiveText;

  div {
    margin: 5px 20px;
    font-size: 1px;
    display: flex;
    justify-content: space-between;
  }
}

</style>