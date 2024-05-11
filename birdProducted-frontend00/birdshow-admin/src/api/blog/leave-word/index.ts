import { message } from 'ant-design-vue'

// 反馈列表
export async function leaveMessageList() {
  return useGet('/leaveWord/back/list').catch(msg => message.warn(msg))
}

// 搜索反馈
export async function searchLeaveMessage(data: any) {
  return usePost('/leaveWord/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过反馈
export async function isCheckLeaveMessage(data: any) {
  return usePost('/leaveWord/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除反馈
export async function deleteLeaveMessage(data: any) {
  return useDelete('/leaveWord/back/delete', JSON.stringify(data)).catch(msg => message.warn(msg))
}
