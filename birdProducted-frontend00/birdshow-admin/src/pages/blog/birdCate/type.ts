import type { VNodeChild } from 'vue'

/**
 * 分类数据类型
 */
export interface CateDataItem {
  // 唯一id
  id?: string | number
  // 菜单唯一key
  key?: string | number
  articleCount?: string | number
  // 分类名
  categoryName: string | (() => VNodeChild)
  // 父级菜单的id
  parentId?: string | number | null
  // 如果使用了隐藏，那么点击当前菜单的时候，可以使用父级的key
  parentKeys?: string[]
  // 子集
  children?: CateDataItem[]
  // 生物分支
  biologyBranch?: string
  // 这里包含所有的父级元素
  matched?: CateDataItem[]
  // 创建时间
  createTime?: string
  // 修改时间
  updateTime?: string
   // 多语言配置
  locale?: string

}

/**
 * 添加分类数据类型
 */
export interface formType {
  id: string
  // 上级菜单
  parentId: string
  // 分类名
  categoryName: string
  // 生物分支
  biologyBranch: string

}

/**
 * 添加分类选择上级菜单表单的数据结构
 */
export interface newItem {
  value: number
  label: string
  children?: newItem[]
}
