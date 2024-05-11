import type { formType } from '~/pages/system/menu/type.ts'

import { message } from 'ant-design-vue'
// 获取分类列表
export async function birdcategoriesList() {
    return useGet('/birdcategories/back/list').catch(msg => message.warn(msg))
}
// 查询文章树形分类
export async function articleCategoryByTree() {
    return useGet('/birdcategories/list').catch(msg => message.warn(msg))
}
// 搜索分类
export async function searchCategory(data: any) {
    return usePost('/birdcategories/back/search', data).catch(msg => message.warn(msg))
}

// 根据id查询分类
export async function searchCategoryById(id: string) {
    return useGet(`/birdcategories/back/get/${id}`).catch(msg => message.warn(msg))
}
// 增添分类
export async function addCategory(data: formType) {
  return usePost<formType>('/birdcategories/back/add', data).catch(msg => message.warn(msg))
}
// 修改分类
export async function updateCategory(data: formType) {
    return usePost<formType>('/birdcategories/back/update', data, {
      customDev: true,
    }).catch(msg => message.warn(msg))
}

  // 删除分类
export async function deleteCategoryByIds(ids: string) {
    return useDelete('/birdcategories/back/delete', JSON.stringify(ids)).catch(msg => message.warn(msg))
}






// 评论列表
export async function commentList() {
  return useGet('/comment/back/list').catch(msg => message.warn(msg))
}



// 搜索评论
export async function searchComment(data: any) {
  return usePost('/comment/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过评论
export async function isCheckComment(data: any) {
  return usePost('/comment/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除评论
export async function deleteComment(id: string) {
  return useDelete(`/comment/back/delete/${id}`).catch(msg => message.warn(msg))
}
