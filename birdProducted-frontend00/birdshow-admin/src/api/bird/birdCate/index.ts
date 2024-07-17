import { message } from 'ant-design-vue'
import type { UnwrapNestedRefs } from 'vue'
import type { formType } from '~/pages/bird/birdCate/type.ts'

// 获取分类列表
export async function birdcategoriesList() {
  return useGet('/birdcategories/back/list').catch(msg => message.warn(msg))
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
export async function addCategory(data: UnwrapNestedRefs<formType>) {
  return usePut<formType>('/birdcategories/back/add', data).catch(msg => message.warn(msg))
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
